package org.test.stores;

import com.tangosol.net.cache.CacheStore;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Uze on 10.12.13.
 */
public abstract class JdbcCacheStore<K, V> implements CacheStore {

    private final String tableName;
    private final DataSource dataSource;
    private final int maxBatchSize;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String insertQuery;
    private final String updateQuery;
    private final String deleteQuery;
    private final String selectQuery;
    private final String selectKeysQuery;
    private final TransactionTemplate txTemplate;

    protected abstract RowMapper<V> getRowMapper();

    protected abstract RowMapper<K> getKeyRowMapper();

    protected abstract K getKey(V value);

    protected abstract Object[] decomposeKey(K key);

    public JdbcCacheStore(String tableName, ColumnInfo[] columnInfos) {
        this.tableName = tableName;
        try {
            Context ctx = new InitialContext();
            this.dataSource = (DataSource) ctx.lookup("jdbc/cacheDS");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.maxBatchSize = 100;
        this.insertQuery = QueryUtils.buildInsertQuery(tableName, columnInfos);
        this.updateQuery = QueryUtils.buildUpdateQuery(tableName, columnInfos);
        this.deleteQuery = QueryUtils.buildDeleteQuery(tableName, columnInfos);
        this.selectQuery = QueryUtils.buildSelectQuery(tableName, columnInfos);
        this.selectKeysQuery = QueryUtils.buildSelectKeysQuery(tableName, columnInfos);
        this.txTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    private int getMaxBatchSize() {
        return maxBatchSize;
    }

    protected NamedParameterJdbcTemplate getTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);// jdbcTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void store(Object key, Object value) {
        storeBatch(Collections.singletonMap((K) key, (V) value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void storeAll(Map map) {
        final Map map2 = map;
        txTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                final int batchSize = getMaxBatchSize();
                if (batchSize == 0 || batchSize >= map2.size()) {
                    storeBatch(map2);
                } else {
                    final Map<K, V> batch = new HashMap<K, V>(batchSize);
                    while (!map2.isEmpty()) {
                        final Iterator<Map.Entry<K, V>> it = map2.entrySet().iterator();
                        while (it.hasNext() && batch.size() < batchSize) {
                            Map.Entry<K, V> entry = it.next();
                            batch.put(entry.getKey(), entry.getValue());
                            it.remove();
                        }

                        if (!batch.isEmpty()) {
                            storeBatch(batch);
                            batch.clear();
                        }
                    }
                }
                return null;
            }
        });
    }

    private void storeBatch(Map<K, V> map) {
        final SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(map.values().toArray());
        final int[] result = getTemplate().batchUpdate(updateQuery, batch);
        int updateCount = result[0];
        if (updateCount == Statement.SUCCESS_NO_INFO) {
            updateCount = batch.length;
        } else if (updateCount == Statement.EXECUTE_FAILED) {
            updateCount = 0;
        }
        if (updateCount < batch.length) {
            Map<K, V> insertMap = map;
            if (updateCount > 0) {
                final MapSqlParameterSource paramSource = new MapSqlParameterSource();
                paramSource.addValue(QueryUtils.PARAM_PRIMARY_KEY_BATCH, map.keySet());
                final List<K> existingKeys = getTemplate().query(selectKeysQuery, paramSource, getKeyRowMapper());
                insertMap = new HashMap<K, V>(map);

                // remove keys of existent rows
                insertMap.keySet().removeAll(existingKeys);
            }

            final SqlParameterSource[] insertBatch = SqlParameterSourceUtils.createBatch(insertMap.values().toArray());
            final int[] insertResult = getTemplate().batchUpdate(insertQuery, insertBatch);
            final int insertCount = insertResult[0];

            if (updateCount + insertCount < batch.length) {
                throw new RuntimeException("\"" + tableName + "\": Incomplete store: " + updateCount +
                        " update(s) + " + insertCount + " insert(s) < " + batch.length + " total batch size");
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void erase(Object key) {
        eraseBatch(Collections.singletonList((K) key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void eraseAll(Collection collection) {
        final int batchSize = getMaxBatchSize();
        if (batchSize == 0 || batchSize >= collection.size()) {
            eraseBatch(collection);
        } else {
            final List<K> batch = new ArrayList<K>(batchSize);
            while (!collection.isEmpty()) {
                final Iterator<K> it = collection.iterator();
                while (it.hasNext() && batch.size() < batchSize) {
                    batch.add(it.next());
                    it.remove();
                }

                if (!batch.isEmpty()) {
                    eraseBatch(batch);
                    batch.clear();
                }
            }
        }
    }

    protected void eraseBatch(Collection<K> keys) {
        final List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (K key : keys) {
            batchArgs.add(decomposeKey(key));
        }
        getTemplate().getJdbcOperations().batchUpdate(deleteQuery, batchArgs);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object load(Object key) {
        return loadBatch(Collections.singletonList((K) key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map loadAll(Collection collection) {
        final Map<K, V> result = new HashMap<K, V>();
        final int batchSize = getMaxBatchSize();

        if (batchSize == 0 || batchSize >= collection.size()) {
            result.putAll(loadBatch(collection));
        } else {
            final List<K> batch = new ArrayList<K>(batchSize);
            while (!collection.isEmpty()) {
                final Iterator<K> it = collection.iterator();
                while (it.hasNext() && batch.size() < batchSize) {
                    batch.add(it.next());
                    it.remove();
                }

                if (!batch.isEmpty()) {
                    result.putAll(loadBatch(batch));
                    batch.clear();
                }
            }
        }

        return result;
    }

    private Map<K, V> loadBatch(Collection<K> keys) {
        final List<V> rows = selectBatch(keys);
        final Map<K, V> result = new HashMap<K, V>(rows.size());
        for (V value : rows) {
            result.put(getKey(value), value);
        }
        return result;
    }

    protected List<V> selectBatch(Collection<K> keys) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue(QueryUtils.PARAM_PRIMARY_KEY_BATCH, keys);
        return getTemplate().query(selectQuery, paramSource, getRowMapper());
    }

    public enum Flag {
        PK, INSERTABLE, UPDATEABLE
    }

    public static class ColumnInfo {

        private final String tableColumnName;
        private final String beanFieldName;
        private final EnumSet<Flag> flags;

        public String getTableColumnName() {
            return tableColumnName;
        }

        public String getBeanFieldName() {
            return beanFieldName;
        }

        public boolean isPk() {
            return flags.contains(Flag.PK);
        }

        public boolean isInsertable() {
            return flags.contains(Flag.INSERTABLE) || isPk();
        }

        public boolean isUpdateable() {
            return flags.contains(Flag.UPDATEABLE) && !isPk();
        }

        public ColumnInfo(String tableColumnName, String beanFieldName, Flag... flags) {
            this.tableColumnName = tableColumnName;
            this.beanFieldName = beanFieldName;
            this.flags = EnumSet.copyOf(Arrays.asList(flags));

            if (isPk() && !this.flags.contains(Flag.INSERTABLE)) {
                this.flags.add(Flag.INSERTABLE);
            }
        }

        public ColumnInfo(String tableColumnName, Flag... flags) {
            this(tableColumnName, Character.toLowerCase(tableColumnName.charAt(0)) + tableColumnName.substring(1), flags);
        }

        public ColumnInfo(String tableColumnName) {
            this(tableColumnName, Flag.INSERTABLE, Flag.UPDATEABLE);
        }
    }
}

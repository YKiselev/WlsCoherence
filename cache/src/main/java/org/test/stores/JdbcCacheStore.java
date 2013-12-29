package org.test.stores;

import com.tangosol.net.cache.CacheStore;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.*;

/**
 * Created by Uze on 10.12.13.
 */
public abstract class JdbcCacheStore<K, V> implements CacheStore {

    private final DataSource dataSource;
    private final int maxBatchSize;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    protected abstract String getMergeQuery();

    //protected abstract String getDeleteQuery();

    protected abstract K getKey(V value);

    protected abstract List<V> selectBatch(Collection<K> keys);

    public JdbcCacheStore() {
        try {
            Context ctx = new InitialContext();
            this.dataSource = (DataSource) ctx.lookup("jdbc/cacheDS");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.maxBatchSize = 100;
    }

    private int getMaxBatchSize() {
        return maxBatchSize;
    }

    protected NamedParameterJdbcTemplate getTemplate() {
        return jdbcTemplate;// new JdbcTemplate(dataSource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void store(Object key, Object value) {
        storeBatch(Collections.singletonMap((K) key, (V) value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void storeAll(Map map) {
        final int batchSize = getMaxBatchSize();
        if (batchSize == 0 || batchSize >= map.size()) {
            storeBatch(map);
        } else {
            final Map<K, V> batch = new HashMap<K, V>(batchSize);
            while (!map.isEmpty()) {
                final Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
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
    }

    private void storeBatch(Map<K, V> map) {
        getTemplate().batchUpdate(getMergeQuery(), SqlParameterSourceUtils.createBatch(map.values().toArray()));
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

    protected abstract void eraseBatch(Collection<K> keys);// {
//        getTemplate().batchUpdate(getDeleteQuery(), SqlParameterSourceUtils.createBatch(keys.toArray()));
//    }

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
}

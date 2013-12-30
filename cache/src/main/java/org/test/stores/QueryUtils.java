package org.test.stores;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uze on 30.12.13.
 */
public final class QueryUtils {

    public static final String PARAM_PRIMARY_KEY_BATCH = "primaryKeyBatch";

    private QueryUtils() {
    }

    /**
     * Builds insert sql statement
     * e.g. INSERT INTO Table1 (Id, Name) VALUES (:id, :name)
     *
     * @param tableName
     * @param columnInfos
     * @return
     */
    public static String buildInsertQuery(String tableName, JdbcCacheStore.ColumnInfo[] columnInfos) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName");
        }
        if (columnInfos == null || columnInfos.length == 0) {
            throw new IllegalArgumentException("columnInfos");
        }
        final StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO ").append(tableName).append(" (");

        int columns = 0;
        for (JdbcCacheStore.ColumnInfo info : columnInfos) {
            if (!info.isInsertable()) {
                continue;
            }

            if (columns > 0) {
                sb.append(',');
            }
            sb.append(info.getTableColumnName());
            columns++;
        }

        sb.append(") VALUES (");

        int vars = 0;
        for (JdbcCacheStore.ColumnInfo info : columnInfos) {
            if (!info.isInsertable()) {
                continue;
            }

            if (vars > 0) {
                sb.append(',');
            }

            sb.append(':').append(info.getBeanFieldName());
            vars++;
        }

        sb.append(")");

        if (columns != vars) {
            throw new RuntimeException("Columns (" + columns + ") != Variables (" + vars + ")");
        }

        return sb.toString();
    }

    /**
     * Builds update sql statement
     * e.g. UPDATE Table1 SET Id = :id, Name=:name
     *
     * @param tableName
     * @param columnInfos
     * @return
     */
    public static String buildUpdateQuery(String tableName, JdbcCacheStore.ColumnInfo[] columnInfos) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName");
        }
        if (columnInfos == null || columnInfos.length == 0) {
            throw new IllegalArgumentException("columnInfos");
        }
        final List<JdbcCacheStore.ColumnInfo> primaryKey = getPrimaryKey(columnInfos);
        if (primaryKey.isEmpty()) {
            throw new IllegalArgumentException("No primary key columns for table \"" + tableName + "\"");
        }
        final StringBuilder sb = new StringBuilder();

        sb.append("UPDATE ").append(tableName).append(" SET ");

        int columns = 0;
        for (JdbcCacheStore.ColumnInfo info : columnInfos) {
            if (!info.isUpdateable()) {
                continue;
            }

            if (columns > 0) {
                sb.append(',');
            }
            sb.append(info.getTableColumnName()).append(" = :").append(info.getBeanFieldName());
            columns++;
        }

        sb.append(" WHERE ");

        int keyFields = 0;
        for (JdbcCacheStore.ColumnInfo info : primaryKey) {
            if (keyFields > 0) {
                sb.append(" AND ");
            }
            sb.append(info.getTableColumnName()).append(" = :").append(info.getBeanFieldName());
            keyFields++;
        }

        return sb.toString();
    }

    /**
     * Builds delete SQL statenemt for batches (no named parameters)
     * e.g. DELETE FROM Table1 WHERE C1 = ? AND C2 = ?
     *
     * @param tableName
     * @param columnInfos
     * @return delete query
     */
    public static String buildDeleteQuery(String tableName, JdbcCacheStore.ColumnInfo[] columnInfos) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName");
        }
        if (columnInfos == null || columnInfos.length == 0) {
            throw new IllegalArgumentException("columnInfos");
        }
        final List<JdbcCacheStore.ColumnInfo> primaryKey = getPrimaryKey(columnInfos);
        if (primaryKey.isEmpty()) {
            throw new IllegalArgumentException("No primary key columns for table \"" + tableName + "\"");
        }

        final StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM ").append(tableName).append(" WHERE ");

        int columns = 0;
        for (JdbcCacheStore.ColumnInfo info : primaryKey) {
            if (columns > 0) {
                sb.append(" AND ");
            }
            sb.append(info.getTableColumnName()).append(" = ?");
            columns++;
        }

        return sb.toString();
    }

    /**
     * Builds select sql query for querying many items i.e. using IN clause (complex if primary key is complex)
     *
     * @param tableName
     * @param columnInfos
     * @return
     */
    public static String buildSelectQuery(String tableName, JdbcCacheStore.ColumnInfo[] columnInfos) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName");
        }
        if (columnInfos == null || columnInfos.length == 0) {
            throw new IllegalArgumentException("columnInfos");
        }
        final List<JdbcCacheStore.ColumnInfo> primaryKey = getPrimaryKey(columnInfos);
        if (primaryKey.isEmpty()) {
            throw new IllegalArgumentException("No primary key columns for table \"" + tableName + "\"");
        }
        final boolean complexFlag = primaryKey.size() > 1;

        final StringBuilder sb = new StringBuilder();

        sb.append("SELECT\n");

        int columns = 0;
        for (JdbcCacheStore.ColumnInfo info : columnInfos) {
            if (columns > 0) {
                sb.append(",");
            }
            sb.append(info.getTableColumnName()).append("\n");
            columns++;
        }

        sb.append("\nFROM ").append(tableName).append("\nWHERE\n");

        if (complexFlag) {
            sb.append('(');
        }

        int keyFields = 0;
        for (JdbcCacheStore.ColumnInfo info : primaryKey) {
            if (keyFields > 0) {
                sb.append(',');
            }
            sb.append(info.getTableColumnName());
            keyFields++;
        }

        if (complexFlag) {
            sb.append(')');
        }
        sb.append(" IN (:").append(PARAM_PRIMARY_KEY_BATCH).append(')');

        return sb.toString();
    }

    /**
     * Builds select sql query for querying existing keys
     * e.g. SELECT Id FROM Table1 WHERE Id IN (:primaryKeyBatch)
     *
     * @param tableName
     * @param columnInfos
     * @return
     */
    public static String buildSelectKeysQuery(String tableName, JdbcCacheStore.ColumnInfo[] columnInfos) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName");
        }
        if (columnInfos == null || columnInfos.length == 0) {
            throw new IllegalArgumentException("columnInfos");
        }
        final List<JdbcCacheStore.ColumnInfo> primaryKey = getPrimaryKey(columnInfos);
        if (primaryKey.isEmpty()) {
            throw new IllegalArgumentException("No primary key columns for table \"" + tableName + "\"");
        }
        final boolean complexFlag = primaryKey.size() > 1;

        final StringBuilder sb = new StringBuilder();

        sb.append("SELECT\n");

        int columns = 0;
        for (JdbcCacheStore.ColumnInfo info : primaryKey) {
            if (columns > 0) {
                sb.append(",");
            }
            sb.append(info.getTableColumnName()).append("\n");
            columns++;
        }

        sb.append("\nFROM ").append(tableName).append("\nWHERE\n");

        if (complexFlag) {
            sb.append('(');
        }

        int keyFields = 0;
        for (JdbcCacheStore.ColumnInfo info : primaryKey) {
            if (keyFields > 0) {
                sb.append(',');
            }
            sb.append(info.getTableColumnName());
            keyFields++;
        }

        if (complexFlag) {
            sb.append(')');
        }

        sb.append(" IN (:").append(PARAM_PRIMARY_KEY_BATCH).append(')');

        return sb.toString();
    }

    /**
     * Extracts primary key columns (columns with PK flag set)
     *
     * @param columnInfos
     * @return
     */
    public static List<JdbcCacheStore.ColumnInfo> getPrimaryKey(JdbcCacheStore.ColumnInfo[] columnInfos) {
        List<JdbcCacheStore.ColumnInfo> result = new ArrayList<JdbcCacheStore.ColumnInfo>();
        for (JdbcCacheStore.ColumnInfo info : columnInfos) {
            if (info.isPk()) {
                result.add(info);
            }
        }
        return result;
    }
}

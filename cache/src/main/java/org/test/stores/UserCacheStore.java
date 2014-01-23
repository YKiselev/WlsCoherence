package org.test.stores;

import org.springframework.jdbc.core.RowMapper;
import org.test.mappers.LongRowMapper;
import org.test.mappers.SimpleRowMapper;
import org.test.mappers.UserRowMapper;
import org.test.pof.UserPO;

/**
 * Created by Юрий on 10.12.13.
 */
public class UserCacheStore extends JdbcCacheStore<Long, UserPO> {

    private static final ColumnInfo[] COLUMN_INFOS = new ColumnInfo[]{
            new ColumnInfo("Id", Flag.PK, Flag.INSERTABLE),
            new ColumnInfo("FirstName"),
            new ColumnInfo("LastName"),
            new ColumnInfo("MiddleName"),
            new ColumnInfo("Login"),
            new ColumnInfo("AddressLine1"),
            new ColumnInfo("AddressLine2"),
            new ColumnInfo("PostIndex"),
            new ColumnInfo("Phone1"),
            new ColumnInfo("CreatedAt", "created", Flag.INSERTABLE),
            new ColumnInfo("UpdatedAt", "updated", Flag.UPDATEABLE),
    };

    @Override
    protected RowMapper<UserPO> getRowMapper() {
        return new UserRowMapper();
    }

    @Override
    protected RowMapper<Long> getKeyRowMapper() {
        return new LongRowMapper();
    }

    @Override
    protected Object[] decomposeKey(Long key) {
        return new Object[]{key};
    }

    public UserCacheStore() {
        super("Users", COLUMN_INFOS);
    }

    @Override
    protected Long getKey(UserPO value) {
        return value.getId();
    }
}

package org.test.stores;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.test.mappers.UserRowMapper;
import org.test.pof.UserPO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Юрий on 10.12.13.
 */
public class UserCacheStore extends JdbcCacheStore<Long, UserPO> {

    @Override
    protected String getMergeQuery() {
        return "MERGE INTO Users\n" +
                "USING DUAL ON (Id = :id)\n" +
                "WHEN MATCHED THEN\n" +
                "  UPDATE SET FirstName = :firstName,\n" +
                "  LastName = :lastName,\n" +
                "  MiddleName = :middleName,\n" +
                "  Login = :login,\n" +
                "  AddressLine1 = :addressLine1,\n" +
                "  AddressLine2 = :addressLine2,\n" +
                "  PostIndex = :postIndex,\n" +
                "  Phone1 = :phone1,\n" +
                "  Updated = :updated\n" +
                "WHEN NOT MATCHED THEN\n" +
                "  INSERT (Id,FirstName,LastName,MiddleName,Login,AddressLine1,AddressLine2,PostIndex,Phone1,Created)" +
                "  VALUES (:id,:firstName,:lastName,:middleName,:login, :addressLine1, :addressLine2, :postIndex, :phone1, :created)";
    }

//    @Override
//    protected String getDeleteQuery() {
//        return "DELETE FROM Users WHERE Id = :longValue";
//    }

    @Override
    protected List<UserPO> selectBatch(Collection<Long> keys) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("Ids", keys);
        return getTemplate().query("SELECT * FROM Users WHERE Id IN (:Ids)", paramSource, new UserRowMapper());
    }

    @Override
    protected Long getKey(UserPO value) {
        return value.getId();
    }

    @Override
    protected void eraseBatch(Collection<Long> keys) {
        final List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (Long key : keys) {
            batchArgs.add(new Object[]{key});
        }
        getTemplate().getJdbcOperations().batchUpdate("DELETE FROM Users WHERE Id = ?", batchArgs);
    }
}

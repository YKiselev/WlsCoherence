package org.test.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.test.pof.UserPO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Uze on 29.12.13.
 */
public class UserRowMapper implements RowMapper<UserPO> {

    @Override
    public UserPO mapRow(ResultSet resultSet, int i) throws SQLException {
        UserPO result = new UserPO();

        result.setId(resultSet.getLong("Id"));
        result.setFirstName(resultSet.getString("FirstName"));
        result.setLastName(resultSet.getString("LastName"));
        result.setMiddleName(resultSet.getString("MiddleName"));
        result.setLogin(resultSet.getString("Login"));
        result.setAddressLine1(resultSet.getString("AddressLine1"));
        result.setAddressLine2(resultSet.getString("AddressLine2"));
        result.setPostIndex(resultSet.getString("PostIndex"));
        result.setPhone1(resultSet.getString("Phone1"));
        result.setCreated(resultSet.getDate("Created"));
        result.setUpdated(resultSet.getDate("Updated"));

        return result;
    }
}

package org.test.mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Uze on 31.12.13.
 */
public class SimpleRowMapper<T> implements RowMapper<T> {

    @Override
    @SuppressWarnings("unchecked")
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return (T) rs.getObject(1);
    }
}

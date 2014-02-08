package org.test.stores;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.test.mappers.LongRowMapper;

import javax.sql.DataSource;

/**
 * Created by Uze on 09.02.14.
 */
public class UserKeyLoader extends KeyLoader {

    private static final String SELECT_ALL = "SELECT Id FROM Users";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Long> rowMapper = new LongRowMapper();

    public UserKeyLoader() {
        final DataSource dataSource = DataSourceFactory.getDataSource();

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Object load(Object o) {
        System.out.println("!!! Loading keys for " + o);
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }
}

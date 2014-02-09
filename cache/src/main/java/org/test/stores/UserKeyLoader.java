package org.test.stores;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.test.UserKeyOperations;
import org.test.mappers.LongRowMapper;
import org.test.pof.SurrogateKey;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by Uze on 09.02.14.
 */
public class UserKeyLoader extends KeyLoader {

    private static final String SELECT_ALL = "SELECT Id FROM Users";
    private static final String SELECT_BY_NAME = SELECT_ALL + " WHERE FirstName = :firstName";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Long> rowMapper = new LongRowMapper();

    public UserKeyLoader() {
        final DataSource dataSource = DataSourceFactory.getDataSource();

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Object load(Object o) {
        final SurrogateKey key = (SurrogateKey) o;
        final Map<String, Object> map = key.toMap();

        switch (key.getOperation(UserKeyOperations.class)) {
            case ALL:
                return selectAll();

            case BY_NAME:
                return selectByName(map);
        }

        throw new IllegalArgumentException("Bad key: " + o);
    }

    private Object selectAll() {
        System.out.println("!!! Loading all User keys");
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    private Object selectByName(Map<String, Object> params) {
        System.out.println("!!! Loading User keys for: " + params);
        return jdbcTemplate.query(SELECT_BY_NAME, params, rowMapper);
    }

}

package org.test.stores;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.CacheStore;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.test.pof.UserPO;

import javax.sql.DataSource;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uze on 31.12.13.
 */
public class TestJdbcCacheStore {

    public static final long COUNT = 10L;

    private final CacheStore cacheStore;
    private final Map<Long, UserPO> users = new HashMap<Long, UserPO>();

    public TestJdbcCacheStore() {
        this.cacheStore = new UserCacheStore();

        for (long i = 1L; i <= COUNT; i++) {
            UserPO user = new UserPO();

            user.setId(i);
            user.setLogin("johndoe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setCreated(new Date());

            users.put(i, user);
        }
    }

    @Before
    public void setUp() throws Exception {
        cacheStore.eraseAll(users.keySet());
    }

    @Test
    @Ignore
    public void testStore() throws Exception {
        for (Map.Entry<Long, UserPO> entry : users.entrySet()) {
            cacheStore.store(entry.getKey(), entry.getValue());
        }
    }

    @Test
    //@Ignore
    public void testStoreAll() throws Exception {
        cacheStore.storeAll(users);
    }

    @Test
    @Ignore
    public void testErase() throws Exception {
        cacheStore.store(1L, users.get(1L));
        cacheStore.erase(1L);
    }

    @Test
    @Ignore
    public void testEraseAll() throws Exception {
        cacheStore.storeAll(users);
        cacheStore.eraseAll(users.keySet());
    }

    @Test
    @Ignore
    public void testBatchUpdate() throws Exception {
        final DataSource ds = DataSourceFactory.getDataSource();
        final Connection con = ds.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement("UPDATE Users SET FirstName = ? WHERE Id = ?");
            try {
                for (UserPO user : users.values()) {
                    ps.setObject(1, user.getFirstName());
                    ps.setObject(2, user.getId());
                    //ps.setString(1, user.getFirstName());
                    //ps.setLong(2, user.getId());
                    ps.addBatch();
                }

                int[] br = ps.executeBatch();

                int uc = ps.getUpdateCount();

                int g = 0;

                con.commit();
            } finally {
                ps.close();
            }
        } finally {
            con.close();
        }
    }

    @Test
    @Ignore
    public void testBinaryEntryStore() throws Exception {
        NamedCache cache = CacheFactory.getCache("Users");

        cache.putAll(users);

        UserPO user = (UserPO)cache.get(1L);

    }
}

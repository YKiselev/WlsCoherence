package org.test.client;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Cluster;
import com.tangosol.net.NamedCache;
import org.test.pof.UserPO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uze on 26.12.13.
 */
public class ClientApp {

    public static final long COUNT = 100L;

    public static void main(String[] args) {
        Cluster cluster = CacheFactory.ensureCluster();
        try {
            NamedCache cache = CacheFactory.getCache("Users");

            Map<Long, UserPO> users = new HashMap<Long, UserPO>();

            for (long i = 1L; i < COUNT; i++) {
                UserPO user = new UserPO();

                user.setId(i);
                user.setLogin("johndoe");
                user.setFirstName("John");
                user.setLastName("Doe");
                user.setCreated(new Date());

                users.put(i, user);
            }

            final long t0 = System.nanoTime();
            for (Map.Entry<Long, UserPO> entry : users.entrySet()) {
                cache.put(entry.getKey(), entry.getValue());
            }
            final long t1 = System.nanoTime();

            cache.clear();

            final long t2 = System.nanoTime();
            cache.putAll(users);
            final long t3 = System.nanoTime();

            System.out.println("Total put() time: " + (t1 - t0) / 1000000L + " ms");
            System.out.println("putAll() time: " + (t3 - t2) / 1000000L + " ms");

            System.out.println("get(1) = " + cache.get(1L));
        } finally {
            cluster.shutdown();
        }
    }
}

package org.test.client;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.AbstractInvocable;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import org.test.pof.MyInvocable1;
import org.test.pof.UserPO;

import java.io.IOException;
import java.util.*;

/**
 * Created by Uze on 26.12.13.
 */
public class ClientApp {

    public static final long COUNT = 100L;

    public static void main(String[] args) {
        //testCachePut();

        //testCacheGet();

        testInvocable();
    }

    private static void testCachePut() {
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

        users.clear();

        for (long i = COUNT; i < 2 * COUNT; i++) {
            UserPO user = new UserPO();

            user.setId(i);
            user.setLogin("johndoe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setCreated(new Date());

            users.put(i, user);
        }

        final long t2 = System.nanoTime();
        cache.putAll(users);
        final long t3 = System.nanoTime();

        System.out.println("Total put() time: " + (t1 - t0) / 1000000L + " ms");
        System.out.println("putAll() time: " + (t3 - t2) / 1000000L + " ms");
    }

    private static void testCacheGet() {
        final NamedCache cache = CacheFactory.getCache("Users");

        final List<Long> ids = new ArrayList<Long>((int)(2 * COUNT));
        for (long i = 1L; i < 2 * COUNT; i++) {
            ids.add(i);
        }

        long t0 = System.nanoTime();
        Map<Long, UserPO> map = cache.getAll(ids);
        long t1 = System.nanoTime();
        System.out.println("getAll() = " + map + ", time: " + (t1 - t0) / 1000000 + " ms");

        System.out.println("get(1) = " + cache.get(1L));
    }

    private static void testInvocable() {
        InvocationService invocationService = (InvocationService) CacheFactory.getService("ExtendTcpInvocationService");
//        Map result = invocationService.query(new AbstractInvocable() {
//            @Override
//            public void run() {
//                setResult(CacheFactory.getCache("Users").get(1L));
//            }
//        }, null);

        //Map result = invocationService.query(new MyInvocable1(), null);

        Map result = invocationService.query(new MyInvocable2(), null);

        System.out.println("Result of invocation: " + result);
    }

    public static class MyInvocable2 extends AbstractInvocable implements PortableObject {

        public MyInvocable2() {
        }

        @Override
        public void run() {
            setResult("All Ok!");
        }

        @Override
        public void readExternal(PofReader pofReader) throws IOException {
        }

        @Override
        public void writeExternal(PofWriter pofWriter) throws IOException {
        }
    }
}

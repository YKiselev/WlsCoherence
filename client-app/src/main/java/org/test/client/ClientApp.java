package org.test.client;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import com.tangosol.net.Service;
import org.test.invocable.ItemOperation;
import org.test.pof.Department;
import org.test.invocable.MyInvocable1;
import org.test.pof.UserPO;
import org.test.vo.ManagerVO;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uze on 26.12.13.
 */
public class ClientApp {

    public static final long COUNT = 100L;

    public static void main(String[] args) {
        Subject subject = null;// = new Subject(true, Collections.singleton(new PofPrincipal("user1")),
        //Collections.emptySet(), Collections.emptySet());

        try {
            final LoginContext loginContext = new LoginContext("client-app", new CallbackHandler() {
                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (int i = 0; i < callbacks.length; i++) {
                        if (callbacks[i] instanceof TextOutputCallback) {
                            // display a message according to a specified type
                        } else if (callbacks[i] instanceof NameCallback) {
                            // prompt the user for a username
                        } else if (callbacks[i] instanceof PasswordCallback) {
                            // prompt the user for a password
                        } else {
                            throw new UnsupportedCallbackException(callbacks[i], "Unrecognized callback");
                        }
                    }
                }
            });

            loginContext.login();

            subject = loginContext.getSubject();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }

        PrivilegedAction pa = new PrivilegedAction() {
            @Override
            public Object run() {
                //while (true) {
                testUserKeys();

                    //testCache();

                    //testCache2();

                    //testCache3();

                    //testInvocable();

//                    try {
//                        Thread.sleep(25);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        break;
//                    }
            //    }
                return null;
            }
        };
        Subject.doAs(subject, pa);
    }

    private static void testCache() {
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

        //cache.clear();

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

        Map<Long, UserPO> map2 = cache.getAll(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        System.out.println("getAll() = " + map2);

        //boolean flag = cache.keySet().removeAll(Arrays.asList(5L, 15L, 25L, 35L, 45L, 55L));
        //System.out.println("removeAll() = " + flag);

        System.out.println("get(1) = " + cache.get(1L));
    }

    private static void testUserKeys() {
        NamedCache cache = CacheFactory.getCache("UserKeys");

        long t0 = System.nanoTime();
        Object result = cache.get(1L); // dummy key, not used in loader
        long t1 = System.nanoTime();

        System.out.println("#1 Keys load time: " + (t1 - t0) / 1000000L + " ms");
        System.out.println("result = " + result);

        t0 = System.nanoTime();
        result = cache.get(1L); // dummy key, not used in loader
        t1 = System.nanoTime();

        // second try while keys in local cache
        System.out.println("#2 Keys load time: " + (t1 - t0) / 1000000L + " ms");
        System.out.println("result = " + result);

        // expire local cache
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // third try while keys in remote cache
        t0 = System.nanoTime();
        result = cache.get(1L); // dummy key, not used in loader
        t1 = System.nanoTime();

        System.out.println("#3 Keys load time: " + (t1 - t0) / 1000000L + " ms");
        System.out.println("result = " + result);

    }

    private static void testCache2() {
        NamedCache cache = CacheFactory.getCache("Departments");

        Map<Long, Department> depts = new HashMap<Long, Department>();

        for (long i = 1L; i < COUNT; i++) {
            Department d = new Department();

            d.setId(i);
            d.setName("John");
            d.setCreated(new Date());

            depts.put(i, d);
        }

        final long t0 = System.nanoTime();
        for (Map.Entry<Long, Department> entry : depts.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        final long t1 = System.nanoTime();

        depts.clear();

        for (long i = COUNT; i < 2 * COUNT; i++) {
            Department d = new Department();

            d.setId(i);
            d.setName("John");
            d.setCreated(new Date());

            depts.put(i, d);
        }

        final long t2 = System.nanoTime();
        cache.putAll(depts);
        final long t3 = System.nanoTime();

        System.out.println("Total put() time: " + (t1 - t0) / 1000000L + " ms");
        System.out.println("putAll() time: " + (t3 - t2) / 1000000L + " ms");

        System.out.println("get(1) = " + cache.get(1L));
    }

    private static void testCache3() {
        NamedCache cache = CacheFactory.getCache("Managers");

        Map<Long, ManagerVO> managers = new HashMap<Long, ManagerVO>();

        for (long i = 1L; i < COUNT; i++) {
            ManagerVO m = new ManagerVO();

            m.setId(i);
            m.setName("John");
            m.setSalary(5000);

            managers.put(i, m);
        }

        final long t0 = System.nanoTime();
        for (Map.Entry<Long, ManagerVO> entry : managers.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        final long t1 = System.nanoTime();

        managers.clear();

        for (long i = COUNT; i < 2 * COUNT; i++) {
            ManagerVO m = new ManagerVO();

            m.setId(i);
            m.setName("John");
            m.setSalary(5000);

            managers.put(i, m);
        }

        final long t2 = System.nanoTime();
        cache.putAll(managers);
        final long t3 = System.nanoTime();

        System.out.println("Total put() time: " + (t1 - t0) / 1000000L + " ms");
        System.out.println("putAll() time: " + (t3 - t2) / 1000000L + " ms");

        System.out.println("get(1) = " + cache.get(1L));
    }

    private static void testInvocable() {
        Service svc = CacheFactory.getService("ExtendTcpProxyService");

        InvocationService invocationService = (InvocationService) CacheFactory.getService("ExtendTcpInvocationService");

        Map result = invocationService.query(new MyInvocable1(), null);
        System.out.println("Result of invocation: " + result);

        String src = "My new item! " + new Date();

        result = invocationService.query(new ItemOperation(src), null);
        System.out.println("Result of invocation: " + result);

        //result = invocationService.query(new AmendOperation(src), null);
        //System.out.println("Result of invocation: " + result);
    }
}

package org.test.invocable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.net.AbstractInvocable;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * org.test.client
 * <p/>
 * Luxoft, Member of the IBS Group, <br/>
 * Deutsche Bank (DB ODC) department <br/>
 * <br/>
 * YKiselev@luxoft.com <br/>
 * yuriy.kiselev@db.com <br/>
 * 3dsf-dev@list.db.com<br/>
 * <p/>
 * User:
 * Date: 23.01.14 <br/>
 * Time: 16:05 <br/>
 */
@Portable
public class MyInvocable1 extends AbstractInvocable {

    @Override
    public void run() {
        System.out.println("!!!!!!!! " + getClass().getName() + " called");

        setResult(Runtime.getRuntime().freeMemory());
        NamedCache cache = CacheFactory.getCache("Users");
        Object res = cache.get(1L);
        System.out.println("cache.get(1L)=" + res);
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (hostname == null) {
            hostname = "user.name=" + System.getProperty("user.name");
        }
        System.out.println(hostname);
        setResult(hostname);
    }
}

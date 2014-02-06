package org.test;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Cluster;
import org.test.beans.MyBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by Uze on 03.02.14.
 */
@Singleton
@Startup
public class ModuleBean {

    private Cluster cluster;
    @EJB
    private MyBean myBean;

    @PostConstruct
    private void postConstruct() {
        System.out.println("!!!!!!!!!!!!" + getClass().getName() + ".postConstruct() called");

//        System.setProperty("tangosol.coherence.cacheconfig", "META-INF/gar-cache-config.xml");
//        System.setProperty("tangosol.pof.config", "cache-api-pof-config.xml");
//        System.setProperty("tangosol.coherence.mode", "prod");
//        System.setProperty("tangosol.pof.enabled", "true");
//
//        cluster = CacheFactory.ensureCluster();
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println("!!!!!!!!!!!!" + getClass().getName() + ".preDestroy() called");

        //CacheFactory.shutdown();
    }

    public void test() throws NamingException {
        System.out.println("!!!!!!!!!!! myBean="+myBean);

        Context ctx = new InitialContext();

        Object obj = ctx.lookup("java:global/cache-ejb-1.0-SNAPSHOT/MyBean");

        System.out.println("!!!!!!!!!!! myBean lookup ="+obj);
    }
}

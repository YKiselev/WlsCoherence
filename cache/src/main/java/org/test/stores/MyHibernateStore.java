package org.test.stores;

import com.tangosol.coherence.hibernate.HibernateCacheStore;
import org.test.HibernateConfig;


/**
 * Created by Uze on 25.01.14.
 */
public class MyHibernateStore extends HibernateCacheStore {

    public MyHibernateStore(String sEntityName) {
        super(sEntityName, HibernateConfig.createSessionFactory());
    }

}

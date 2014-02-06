package org.test.stores;

import com.tangosol.coherence.hibernate.HibernateCacheStore;
import org.test.HibernateConfig;

import java.util.Collection;
import java.util.Map;


/**
 * Created by Uze on 25.01.14.
 */
public class MyHibernateStore extends HibernateCacheStore {

    public MyHibernateStore(String sEntityName) {
        super(sEntityName, HibernateConfig.getSessionFactory());
    }

    @Override
    public void store(Object key, Object value) {
        super.store(key, value);
    }

    @Override
    public void storeAll(Map entries) {
        super.storeAll(entries);
    }

    @Override
    public void erase(Object key) {
        super.erase(key);
    }

    @Override
    public void eraseAll(Collection keys) {
        super.eraseAll(keys);
    }
}

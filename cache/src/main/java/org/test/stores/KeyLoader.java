package org.test.stores;

import com.tangosol.net.cache.CacheLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uze on 29.01.14.
 */
public abstract class KeyLoader implements CacheLoader {

    @Override
    public Map loadAll(Collection collection) {
        Map<Object, Object> result = new HashMap<Object, Object>();
        for (Object key : collection) {
            result.put(key, load(key));
        }
        return result;
    }
}

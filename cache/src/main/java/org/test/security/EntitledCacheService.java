package org.test.security;

import com.tangosol.net.CacheService;
import com.tangosol.net.WrapperCacheService;

/**
 * Created by Uze on 25.01.14.
 */
public class EntitledCacheService extends WrapperCacheService {

    public EntitledCacheService(CacheService service) {
        super(service);
        System.out.println("!!!!!!!!!!!!!!!" + getClass().getName() + ".<init>() called");
    }
}

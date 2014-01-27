package org.test.security;

import com.tangosol.net.InvocationService;
import com.tangosol.net.WrapperInvocationService;

/**
 * Created by Uze on 25.01.14.
 */
public class EntitledInvocationService extends WrapperInvocationService {

    public EntitledInvocationService(InvocationService service) {
        super(service);
        System.out.println("!!!!!!!!!!!!!!!" + getClass().getName() + ".<init>() called");
    }
}

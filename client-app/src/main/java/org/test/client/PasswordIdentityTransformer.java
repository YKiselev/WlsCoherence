package org.test.client;

import com.tangosol.net.Service;
import com.tangosol.net.security.IdentityTransformer;

import javax.security.auth.Subject;

/**
 * Created by Uze on 25.01.14.
 */
public class PasswordIdentityTransformer implements IdentityTransformer {

    public PasswordIdentityTransformer() {
        System.out.println("!!!!!!!!!!!!!!!" + getClass().getName() + ".<init>() called");
    }

    @Override
    public Object transformIdentity(Subject subject, Service service) throws SecurityException {
        System.out.println("PID TRANSFORMER: " + subject + ", service=" + service);
        return new Object[]{"12345678".toCharArray(), "user1"};
    }
}

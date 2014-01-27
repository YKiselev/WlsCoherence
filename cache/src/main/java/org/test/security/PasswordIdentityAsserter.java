package org.test.security;

import com.tangosol.io.pof.PofPrincipal;
import com.tangosol.net.Service;
import com.tangosol.net.security.IdentityAsserter;

import javax.security.auth.Subject;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Uze on 25.01.14.
 */
public class PasswordIdentityAsserter implements IdentityAsserter {

    public PasswordIdentityAsserter() {
        System.out.println("!!!!!!!!!!!!!!!" + getClass().getName() + ".<init>() called");
    }

    @Override
    public Subject assertIdentity(Object o, Service service) throws SecurityException {
        System.out.println("PID ASSERTER: " + o + ", service=" + service);
        if (o == null) {
            throw new SecurityException("Identity is null!");
        }
        return new Subject(true, Collections.singleton(new PofPrincipal("user1")),
                Collections.emptySet(), Collections.emptySet());
    }
}

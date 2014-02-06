package org.test.security;

import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.WrapperInvocationService;
import org.test.invocable.ServiceFactory;
import org.test.listener.ServiceManagerImpl;

import java.util.Map;
import java.util.Set;

/**
 * Created by Uze on 25.01.14.
 */
public class EntitledInvocationService extends WrapperInvocationService {

    public EntitledInvocationService(InvocationService service) {
        super(service);
        System.out.println("!!!!!!!!!!!!!!!" + getClass().getName() + ".<init>() called in thread " + Thread.currentThread());

        ServiceFactory.setManager(new ServiceManagerImpl());

        System.out.println("!!! ServiceManager set to " + ServiceFactory.getManager());

    }

    @Override
    public Map query(Invocable task, Set setMembers) {
        System.out.println("!!!!!!!!!!!!!!!" + getClass().getName() + ".query() called in thread " + Thread.currentThread());
        return super.query(task, setMembers);
    }
}

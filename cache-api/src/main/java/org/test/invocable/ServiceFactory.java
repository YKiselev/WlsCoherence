package org.test.invocable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Uze on 01.02.14.
 */
public final class ServiceFactory {

    private static final AtomicReference<ServiceManager> manager = new AtomicReference<ServiceManager>();

    public static ServiceManager getManager() {
        return manager.get();
    }

    public static boolean setManager(ServiceManager manager) {
        return ServiceFactory.manager.compareAndSet(null, manager);
    }

    private ServiceFactory() {
    }

}

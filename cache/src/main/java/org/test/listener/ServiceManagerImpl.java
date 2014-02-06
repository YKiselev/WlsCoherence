package org.test.listener;

import org.test.invocable.ServiceManager;

/**
 * Created by Uze on 01.02.14.
 */
public class ServiceManagerImpl implements ServiceManager {

    @Override
    public Object amend(Object source) {
        System.out.println("!!!!! " + getClass().getName() + ".amend(" + source + ")");
        return source;
    }
}

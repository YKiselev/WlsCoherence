package org.test.listener;

import com.tangosol.application.Context;
import com.tangosol.application.LifecycleListener;

/**
 * Created by Uze on 27.12.13.
 */
public class AppLifecycleListener implements LifecycleListener {

    public AppLifecycleListener() {
        System.out.println(getClass().getSimpleName()+".init<>");
    }

    @Override
    public void preStart(Context context) {
        System.out.println(getClass().getSimpleName()+".preStart()");
    }

    @Override
    public void postStart(Context context) {
        System.out.println(getClass().getSimpleName()+".postStart()");
    }

    @Override
    public void preStop(Context context) {
        System.out.println(getClass().getSimpleName()+".preStop()");
    }

    @Override
    public void postStop(Context context) {
        System.out.println(getClass().getSimpleName()+".postStop()");
    }
}

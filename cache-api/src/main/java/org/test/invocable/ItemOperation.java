package org.test.invocable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;

/**
 * Created by Uze on 01.02.14.
 */
@Portable
public class ItemOperation implements Invocable {

    private transient InvocationService invocationService;
    @PortableProperty(0)
    private Object item;
    @PortableProperty(1)
    private Object result;


    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public ItemOperation() {
    }

    public ItemOperation(Object item) {
        this.item = item;
    }

    @Override
    public void init(InvocationService invocationService) {
        this.invocationService = invocationService;
    }

    @Override
    public void run() {
        System.out.println("run(" + item + ") in thread " + Thread.currentThread());

        ServiceManager serviceManager = ServiceFactory.getManager();

        System.out.println("Using ServiceManager=" + serviceManager);
        if (serviceManager != null) {
            result = serviceManager.amend(item);
        }

        System.out.println("run(" + item + ") complete!");
    }

    @Override
    public Object getResult() {
        return result;
    }
}

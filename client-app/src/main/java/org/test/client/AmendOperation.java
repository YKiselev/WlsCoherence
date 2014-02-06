package org.test.client;

import org.test.invocable.ItemOperation;

/**
 * Created by Uze on 01.02.14.
 */
public class AmendOperation extends ItemOperation {

    public AmendOperation(Object item) {
        super(item);
    }

    @Override
    public void run() {
        System.out.println("MWAHAHA!!!" + getClass().getName());
        super.run();
    }
}

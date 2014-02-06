package org.test.beans;

import javax.ejb.Stateless;

/**
 * Created by Uze on 04.02.14.
 */
@Stateless
public class MyBean {

    public void test() {
        System.out.println("!!!!!!!" + getClass().getName() + ".test()");
    }
}

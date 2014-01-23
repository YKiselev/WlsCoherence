package org.test.pof;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.net.AbstractInvocable;

import java.io.Serializable;

/**
 * org.test.client
 * <p/>
 * Luxoft, Member of the IBS Group, <br/>
 * Deutsche Bank (DB ODC) department <br/>
 * <br/>
 * YKiselev@luxoft.com <br/>
 * yuriy.kiselev@db.com <br/>
 * 3dsf-dev@list.db.com<br/>
 * <p/>
 * User:
 * Date: 23.01.14 <br/>
 * Time: 16:05 <br/>
 */
@Portable
public class MyInvocable1 extends AbstractInvocable {

    @Override
    public void run() {
        setResult(Runtime.getRuntime().freeMemory());
    }
}

package org.test.vo;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

/**
 * org.test.pof
 * <p/>
 * Luxoft, Member of the IBS Group, <br/>
 * Deutsche Bank (DB ODC) department <br/>
 * <br/>
 * YKiselev@luxoft.com <br/>
 * yuriy.kiselev@db.com <br/>
 * 3dsf-dev@list.db.com<br/>
 * <p/>
 * User:
 * Date: 30.01.14 <br/>
 * Time: 12:07 <br/>
 */
@Portable
public class ManagerVO {

    @PortableProperty(0)
    private long id;
    @PortableProperty(1)
    private String name;
    @PortableProperty(2)
    private int salary;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public ManagerVO() {
    }
}

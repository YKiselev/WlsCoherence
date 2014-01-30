package org.test.pof;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "Managers")
@Portable
public class Manager {

    @PortableProperty(0)
    @Id
    @Column(name = "Id")
    long id;
    @PortableProperty(1)
    @Column(name = "Name")
    private String name;
    @PortableProperty(2)
    @Column(name = "Salary")
    private int salary;

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

    public Manager() {
    }
}

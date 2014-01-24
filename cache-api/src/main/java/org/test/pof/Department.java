package org.test.pof;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Uze on 24.01.14.
 */
@Portable
@Entity
@Table(name = "Departments")
public class Department {

    @PortableProperty(1)
    @Id
    private long id;
    @PortableProperty(2)
    @Column(name = "Name")
    private String name;
    @PortableProperty(3)
    @Column(name = "CreatedAt")
    private Date created;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Department() {
    }
}

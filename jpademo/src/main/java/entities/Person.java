/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author marcg
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer p_id;
    private String name;
    private int year;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    List<Fee> fees;

    @ManyToMany(mappedBy = "persons", cascade = CascadeType.PERSIST)
    List<SwimStyle> styles;

    public Person() {
    }

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
        this.fees = new ArrayList();
        this.styles = new ArrayList();
    }

    public Address getAddress() {
        return address;
    }

    public void addSwimStyle(SwimStyle style) {
        if (style != null) {
            this.styles.add(style);
            style.getPersons().add(this);
        }
    }

    public void removeSwimStyle(SwimStyle style) {
        if (style != null) {
            this.styles.remove(style);
            style.getPersons().remove(this);
        }
    }

    public List<Fee> getFees() {
        return fees;
    }

    public void addFee(Fee fee) {
        this.fees.add(fee);
        if (fee != null) {
            fee.setPerson(this);
        }
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.setPerson(this);
        }
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}

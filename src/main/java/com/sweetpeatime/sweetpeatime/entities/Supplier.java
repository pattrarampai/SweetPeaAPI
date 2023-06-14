package com.sweetpeatime.sweetpeatime.entities;

import javax.persistence.*;

@Entity
@Table(name="Supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "defaultActive")
    private Integer defaultActive;

    public Supplier(){}

    public Supplier(Integer id, String name, String address, Integer defaultActive) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.defaultActive = defaultActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDefaultActive() {
        return defaultActive;
    }

    public void setDefaultActive(Integer defaultActive) {
        this.defaultActive = defaultActive;
    }
}

package com.sweetpeatime.sweetpeatime.entities;

import javax.persistence.*;

@Entity
@Table(name="Pack")
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer flowerId;
    private Integer unitPack;
    private String unit;
    private String packWeight;
    private String size;

    public Pack(){}

    public Pack(Integer id, String name, Integer flowerId, Integer unitPack, String unit, String packWeight, String size) {
        this.id = id;
        this.name = name;
        this.flowerId = flowerId;
        this.unitPack = unitPack;
        this.unit = unit;
        this.packWeight = packWeight;
        this.size = size;
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

    public Integer getFlowerId() {
        return flowerId;
    }

    public void setFlowerId (Integer flowerId) {
        this.flowerId = flowerId;}

    public Integer getUnitPack() {
        return unitPack;
    }
    public void setUnitPack(Integer unitPack) {
        this.unitPack = unitPack;}


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPackWeight() {
        return packWeight;
    }

    public void setPackWeight(String packWeight) {
        this.packWeight = packWeight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
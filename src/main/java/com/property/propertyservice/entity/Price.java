package com.property.propertyservice.entity;

public class Price {
    private int id;
    private float electricity;
    private float water;
    private float gas;
    private float heating;
    private float management;
    private float parking;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getElectricity() {
        return electricity;
    }

    public void setElectricity(float electricity) {
        this.electricity = electricity;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getGas() {
        return gas;
    }

    public void setGas(float gas) {
        this.gas = gas;
    }

    public float getHeating() {
        return heating;
    }

    public void setHeating(float heating) {
        this.heating = heating;
    }

    public float getManagement() {
        return management;
    }

    public void setManagement(float management) {
        this.management = management;
    }

    public float getParking() {
        return parking;
    }

    public void setParking(float parking) {
        this.parking = parking;
    }
}

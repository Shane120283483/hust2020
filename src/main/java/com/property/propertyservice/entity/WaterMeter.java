package com.property.propertyservice.entity;

public class WaterMeter {
    private int id;
    private int houseId;
    private float currentData;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public float getCurrentData() {
        return currentData;
    }

    public void setCurrentData(float currentData) {
        this.currentData = currentData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

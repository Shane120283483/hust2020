package com.property.propertyservice.entity;

public class Parking {
    private int parkingId;
    private String address;
    private String carId;
    private String owner;
    private String phone;

    public int getParkingId() {
        return parkingId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}

package com.property.propertyservice.service;

import com.property.propertyservice.entity.Parking;

import java.util.List;

public interface ParkingService {
    void addParking(Parking parking);
    List<Parking> getAll();
    void batchDelete(List<Integer> ids);
    void update(Parking parking);
}

package com.property.propertyservice.service;

import com.property.propertyservice.entity.ParkingRecord;

import java.util.List;

public interface ParkingRecordService {
    void addParkingRecord(ParkingRecord parkingRecord);
    List<ParkingRecord> getAll();
    void batchDelete(List<Integer> ids);
    void update(ParkingRecord parkingRecord);
}

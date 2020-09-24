package com.property.propertyservice.service;

import com.property.propertyservice.entity.ElectricityMeter;

import java.util.List;

public interface ElectricityMeterService {
    void addElectricityMeter(ElectricityMeter electricityMeter);
    List<ElectricityMeter> getAll();
    void batchDelete(List<Integer> ids);
    void update(ElectricityMeter electricityMeter);
}

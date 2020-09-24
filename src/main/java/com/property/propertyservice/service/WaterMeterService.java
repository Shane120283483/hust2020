package com.property.propertyservice.service;

import com.property.propertyservice.entity.WaterMeter;

import java.util.List;

public interface WaterMeterService {
    void addWaterMeter(WaterMeter waterMeter);
    List<WaterMeter> getAll();
    void batchDelete(List<Integer> ids);
    void update(WaterMeter waterMeter);
}

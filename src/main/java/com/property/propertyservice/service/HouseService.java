package com.property.propertyservice.service;

import com.property.propertyservice.entity.House;

import java.util.List;

public interface HouseService {
    void addHouse(House house);
    List<House> getAll();
    void batchDelete(List<Integer> ids);
    void update(House house);
}

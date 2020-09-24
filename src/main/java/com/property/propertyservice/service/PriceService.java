package com.property.propertyservice.service;

import com.property.propertyservice.entity.Price;

import java.util.List;

public interface PriceService {
    void addPrice(Price price);
    List<Price> getAll();
    void batchDelete(List<Integer> ids);
    void update(Price price);
}

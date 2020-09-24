package com.property.propertyservice.service;

import com.property.propertyservice.entity.GasMeter;

import java.util.List;

public interface GasMeterService {
    void addGasMeter(GasMeter gasMeter);
    List<GasMeter> getAll();
    void batchDelete(List<Integer> ids);
    void update(GasMeter gasMeter);
}

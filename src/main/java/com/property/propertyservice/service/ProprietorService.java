package com.property.propertyservice.service;

import com.property.propertyservice.entity.Proprietor;

import java.util.List;

public interface ProprietorService {
    List<Proprietor> getAll();
    void addProprietor(Proprietor proprietor);
    void batchDelete(List<String> idList);
    void update(Proprietor proprietor);
}

package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.ElectricityMeter;
import com.property.propertyservice.service.ElectricityMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ElectricityMeterServiceImpl implements ElectricityMeterService {
    @Autowired
    RedisTemplate redisTemplate;

    private boolean checkMonth(String a, String b) {
        LocalDate tmpa = LocalDate.parse(a);
        LocalDate tmpb = LocalDate.parse(b);
        return tmpa.getMonth() == tmpb.getMonth() 
            && tmpa.getYear() == tmpb.getYear();
    }
    

    @Override
    public void addElectricityMeter(ElectricityMeter electricityMeter) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.ELECTRICITY_METER_LIST);
        List<ElectricityMeter> electricityMeters = listOperations.range(0,listOperations.size()-1);

        electricityMeter.setId(getId(electricityMeters));

        for (ElectricityMeter i: electricityMeters) {
            if (i.getHouseId() == electricityMeter.getHouseId() 
            && checkMonth(i.getDate(), electricityMeter.getDate()))
                throw new RuntimeException("该户本月的记录已存在");
        }

        if(getIndex(electricityMeter,electricityMeters)==null){
            listOperations.leftPush(electricityMeter);
        }else {
            throw new RuntimeException("添加的电表记录已存在");
        }
    }

    @Override
    public List<ElectricityMeter> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.ELECTRICITY_METER_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        ElectricityMeter electricityMeter = new ElectricityMeter();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.ELECTRICITY_METER_LIST);

        ids.forEach(id -> {
            electricityMeter.setId(id);
            Integer index = getIndex(electricityMeter,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(ElectricityMeter electricityMeter) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.ELECTRICITY_METER_LIST);
        List<ElectricityMeter> electricityMeters = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(electricityMeter,electricityMeters);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,electricityMeter);
    }

    private static Integer getIndex(ElectricityMeter electricityMeter,
                                    List<ElectricityMeter> electricityMeters){
        Integer index=null;

        for(int i=0;i<electricityMeters.size();i++){
            if(electricityMeters.get(i).getId() == electricityMeter.getId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<ElectricityMeter> objects) {
        int id=-1;

        for(ElectricityMeter data:objects) {
            if(data.getId()>id)
                id = data.getId();
        }

        return id+1;
    }

}

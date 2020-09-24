package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.WaterMeter;
import com.property.propertyservice.service.WaterMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WaterMeterServiceImpl implements WaterMeterService {
    @Autowired
    RedisTemplate redisTemplate;

    private boolean checkMonth(String a, String b) {
        LocalDate tmpa = LocalDate.parse(a);
        LocalDate tmpb = LocalDate.parse(b);
        return tmpa.getMonth() == tmpb.getMonth() 
            && tmpa.getYear() == tmpb.getYear();
    }

    @Override
    public void addWaterMeter(WaterMeter waterMeter) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.WATER_METER_LIST);
        List<WaterMeter> waterMeters = listOperations.range(0,listOperations.size()-1);

        waterMeter.setId(getId(waterMeters));

        for (WaterMeter i: waterMeters) {
            if (i.getHouseId() == waterMeter.getHouseId() 
            && checkMonth(i.getDate(), waterMeter.getDate()))
                throw new RuntimeException("该户本月的记录已存在");
        }

        if(getIndex(waterMeter,waterMeters)==null){
            listOperations.leftPush(waterMeter);
        }else {
            throw new RuntimeException("添加的水表记录已存在");
        }
    }

    @Override
    public List<WaterMeter> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.WATER_METER_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        WaterMeter waterMeter = new WaterMeter();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.WATER_METER_LIST);

        ids.forEach(id -> {
            waterMeter.setId(id);
            Integer index = getIndex(waterMeter,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(WaterMeter waterMeter) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.WATER_METER_LIST);
        List<WaterMeter> waterMeters = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(waterMeter,waterMeters);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,waterMeter);
    }

    private static Integer getIndex(WaterMeter waterMeter, List<WaterMeter> waterMeters){
        Integer index=null;

        for(int i=0;i<waterMeters.size();i++){
            if(waterMeters.get(i).getId() == waterMeter.getId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<WaterMeter> objects) {
        int id=-1;

        for(WaterMeter data:objects) {
            if(data.getId()>id)
                id = data.getId();
        }

        return id+1;
    }

}

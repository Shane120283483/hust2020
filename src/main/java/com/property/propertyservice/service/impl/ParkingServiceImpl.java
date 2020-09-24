package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.Parking;
import com.property.propertyservice.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void addParking(Parking parking) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_LIST);
        List<Parking> parkings = listOperations.range(0,listOperations.size()-1);

        parking.setParkingId(getId(parkings));

        if(getIndex(parking,parkings)==null){
            listOperations.leftPush(parking);
        }else {
            throw new RuntimeException("添加的停车位已存在");
        }
    }

    @Override
    public List<Parking> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        Parking parking = new Parking();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_LIST);

        ids.forEach(id -> {
            parking.setParkingId(id);
            Integer index = getIndex(parking,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(Parking parking) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_LIST);
        List<Parking> parkings = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(parking,parkings);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,parking);
    }

    private static Integer getIndex(Parking parking, List<Parking> parkings){
        Integer index=null;

        for(int i=0;i<parkings.size();i++){
            if(parkings.get(i).getParkingId() == parking.getParkingId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<Parking> objects) {
        int id=-1;

        for(Parking data:objects) {
            if(data.getParkingId()>id)
                id = data.getParkingId();
        }

        return id+1;
    }
}

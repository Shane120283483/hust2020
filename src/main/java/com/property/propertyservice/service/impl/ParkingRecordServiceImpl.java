package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.ParkingRecord;
import com.property.propertyservice.service.ParkingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingRecordServiceImpl implements ParkingRecordService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<ParkingRecord> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_RECORD_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void addParkingRecord(ParkingRecord parkingRecord) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_RECORD_LIST);
        List<ParkingRecord> parkingRecords = listOperations.range(0,listOperations.size()-1);

        parkingRecord.setId(getId(parkingRecords));

        if(getIndex(parkingRecord,parkingRecords)==null){
            listOperations.leftPush(parkingRecord);
        }else {
            throw new RuntimeException("添加的停车记录已存在");
        }
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        ParkingRecord parkingRecord = new ParkingRecord();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_RECORD_LIST);

        ids.forEach(id -> {
            parkingRecord.setId(id);
            Integer index = getIndex(parkingRecord,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(ParkingRecord parkingRecord) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PARKING_RECORD_LIST);
        List<ParkingRecord> parkingRecords = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(parkingRecord,parkingRecords);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,parkingRecord);
    }

    private static Integer getIndex(ParkingRecord parkingRecord, List<ParkingRecord> parkingRecords){
        Integer index=null;

        for(int i=0;i<parkingRecords.size();i++){
            if(parkingRecords.get(i).getId() == parkingRecord.getId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<ParkingRecord> objects) {
        int id=-1;

        for(ParkingRecord data:objects) {
            if(data.getId()>id)
                id = data.getId();
        }

        return id+1;
    }

}

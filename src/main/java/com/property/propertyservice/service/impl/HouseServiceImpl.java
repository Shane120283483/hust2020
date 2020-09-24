package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.House;
import com.property.propertyservice.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void addHouse(House house) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.HOUSE_LIST);
        List<House> houses = listOperations.range(0,listOperations.size()-1);

        house.setId(getId(houses));

        if(getIndex(house,houses)==null){
            listOperations.leftPush(house);
        }else {
            throw new RuntimeException("添加的房屋记录已存在");
        }
    }

    @Override
    public List<House> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.HOUSE_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        House house = new House();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.HOUSE_LIST);

        ids.forEach(id -> {
            house.setId(id);
            Integer index = getIndex(house,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(House house) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.HOUSE_LIST);
        List<House> houses = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(house,houses);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,house);
    }

    private static Integer getIndex(House house, List<House> houses){
        Integer index=null;

        for(int i=0;i<houses.size();i++){
            if(houses.get(i).getId() == house.getId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<House> objects) {
        int id=-1;

        for(House data:objects) {
            if(data.getId()>id)
                id = data.getId();
        }

        return id+1;
    }

}

package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.Price;
import com.property.propertyservice.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void addPrice(Price price) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PRICE_LIST);
        List<Price> prices = listOperations.range(0,listOperations.size()-1);

        price.setId(getId(prices));

        if(getIndex(price,prices)==null){
            listOperations.leftPush(price);
        }else {
            throw new RuntimeException("添加的物价记录已存在");
        }
    }

    @Override
    public List<Price> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PRICE_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        Price price = new Price();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PRICE_LIST);

        ids.forEach(id -> {
            price.setId(id);
            Integer index = getIndex(price,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(Price price) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PRICE_LIST);
        List<Price> prices = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(price,prices);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,price);
    }

    private static Integer getIndex(Price price, List<Price> prices){
        Integer index=null;

        for(int i=0;i<prices.size();i++){
            if(prices.get(i).getId() == price.getId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<Price> objects) {
        int id=-1;

        for(Price data:objects) {
            if(data.getId()>id)
                id = data.getId();
        }

        return id+1;
    }

}

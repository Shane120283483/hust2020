package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.Proprietor;
import com.property.propertyservice.service.ProprietorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProprietorServiceImpl implements ProprietorService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Proprietor> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PROPRIETOR_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void addProprietor(Proprietor proprietor) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PROPRIETOR_LIST);
        List<Proprietor> proprietors = listOperations.range(0,listOperations.size()-1);
        if(getIndex(proprietor,proprietors)==null){
            listOperations.leftPush(proprietor);
        }else {
            throw new RuntimeException("添加的业主已存在");
        }
    }

    @Override
    public void batchDelete(List<String> idList) {
        Proprietor proprietor = new Proprietor();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PROPRIETOR_LIST);

        idList.forEach(id -> {
            proprietor.setIdCard(id);
            Integer index = getIndex(proprietor,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(Proprietor proprietor) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.PROPRIETOR_LIST);
        List<Proprietor> proprietors = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(proprietor,proprietors);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,proprietor);
    }

    private static Integer getIndex(Proprietor proprietor, List<Proprietor> proprietors){
        Integer index=null;

        for(int i=0;i<proprietors.size();i++){
            if(proprietors.get(i).getIdCard().equals(proprietor.getIdCard())){
                index=i;
            }
        }
        return index;
    }
}

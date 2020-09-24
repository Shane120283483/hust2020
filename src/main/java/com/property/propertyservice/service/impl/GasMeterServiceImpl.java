package com.property.propertyservice.service.impl;

import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.GasMeter;
import com.property.propertyservice.service.GasMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GasMeterServiceImpl implements GasMeterService {
    @Autowired
    RedisTemplate redisTemplate;
    
    private boolean checkMonth(String a, String b) {
        LocalDate tmpa = LocalDate.parse(a);
        LocalDate tmpb = LocalDate.parse(b);
        return tmpa.getMonth() == tmpb.getMonth() 
            && tmpa.getYear() == tmpb.getYear();
    }

    @Override
    public void addGasMeter(GasMeter gasMeter) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.GAS_METER_LIST);
        List<GasMeter> gasMeters = listOperations.range(0,listOperations.size()-1);

        gasMeter.setId(getId(gasMeters));

        for (GasMeter i: gasMeters) {
            if (i.getHouseId() == gasMeter.getHouseId() 
            && checkMonth(i.getDate(), gasMeter.getDate()))
                throw new RuntimeException("该户本月的记录已存在");
        }

        if(getIndex(gasMeter,gasMeters)==null){
            listOperations.leftPush(gasMeter);
        }else {
            throw new RuntimeException("添加的煤气记录已存在");
        }
    }

    @Override
    public List<GasMeter> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.GAS_METER_LIST);

        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        GasMeter gasMeter = new GasMeter();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.GAS_METER_LIST);

        ids.forEach(id -> {
            gasMeter.setId(id);
            Integer index = getIndex(gasMeter,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的信息不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(GasMeter gasMeter) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.GAS_METER_LIST);
        List<GasMeter> gasMeters = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(gasMeter,gasMeters);
        if(index == null) {
            throw new RuntimeException("修改的信息不存在");
        }
        listOperations.set(index,gasMeter);
    }

    private static Integer getIndex(GasMeter gasMeter, List<GasMeter> gasMeters){
        Integer index=null;

        for(int i=0;i<gasMeters.size();i++){
            if(gasMeters.get(i).getId() == gasMeter.getId()){
                index=i;
            }
        }
        return index;
    }

    public static int getId(List<GasMeter> objects) {
        int id=-1;

        for(GasMeter data:objects) {
            if(data.getId()>id)
                id = data.getId();
        }

        return id+1;
    }

}

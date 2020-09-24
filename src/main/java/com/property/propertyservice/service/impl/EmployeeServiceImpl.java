package com.property.propertyservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.property.propertyservice.constant.RedisKeyConstant;
import com.property.propertyservice.entity.Employee;
import com.property.propertyservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Employee> getAll() {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.EMPLOYEE_LIST);
        return listOperations.range(0,listOperations.size()-1);
    }

    @Override
    public void addEmployee(Employee employee) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.EMPLOYEE_LIST);
        List<Employee> employees = listOperations.range(0,listOperations.size()-1);
        if(getIndex(employee,employees)==null){
            listOperations.leftPush(employee);
        }else {
            throw new RuntimeException("添加的员工已存在");
        }
    }

    @Override
    public void batchDelete(List<String> accounts) {
        Employee employee = new Employee();
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.EMPLOYEE_LIST);

        accounts.forEach(account -> {
            employee.setAccount(account);
            Integer index = getIndex(employee,listOperations.range(0,listOperations.size()-1));

            if(index == null) {
                throw new RuntimeException("删除的用户不存在");
            }
            listOperations.remove(index,listOperations.index(index));
        });
    }

    @Override
    public void update(Employee employee) {
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.EMPLOYEE_LIST);
        List<Employee> employees = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(employee,employees);
        if(index == null) {
            throw new RuntimeException("修改的用户不存在");
        }
        listOperations.set(index,employee);
    }

    @Override
    public String login(String userName, String password) {
        Employee employee = new Employee();
        employee.setAccount(userName);
        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.EMPLOYEE_LIST);
        List<Employee> employees = listOperations.range(0,listOperations.size()-1);
        Integer index = getIndex(employee,employees);

        if(index == null) {
            throw new RuntimeException("用户不存在，请检查登录信息");
        } else if(!employees.get(index).getPassword().equals(password)) {
            throw new RuntimeException("用户密码错误，请检查登录信息");
        }

        Date start = new Date();
        long lastTime = System.currentTimeMillis() + 60* 60 * 1000*48;//48小时有效时间
        Date end = new Date(lastTime);
        String token;

        token = JWT.create().withAudience(userName)
                .withIssuedAt(start)
                .withExpiresAt(end)
                .sign(Algorithm.HMAC256(password));
        addToken(token,redisTemplate.boundListOps(RedisKeyConstant.TOKEN_LIST));
        return token;
    }

    @Override
    public void logout(String username) {
        BoundListOperations listOperations = redisTemplate.boundListOps(RedisKeyConstant.TOKEN_LIST);
        Integer index = getTokenIndex(username,listOperations);

        if(index == null) {
            return;
        }

        listOperations.remove(index,listOperations.index(index));
    }

    @Override
    public boolean checkToken(String userName, String token) {
        String tokenUser = JWT.decode(token).getAudience().get(0);

        if(!userName.equals(tokenUser) || null == getTokenIndex(userName,
                redisTemplate.boundListOps(RedisKeyConstant.TOKEN_LIST))) {
            throw new RuntimeException("token失效，请重新登录");
        }

        BoundListOperations listOperations =
                redisTemplate.boundListOps(RedisKeyConstant.EMPLOYEE_LIST);
        List<Employee> employees = listOperations.range(0,listOperations.size()-1);
        Employee employee = new Employee();
        employee.setAccount(userName);
        Integer index = getIndex(employee,employees);

        if(index == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }

        // 验证token
        employee = employees.get(index);
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(employee.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            throw new RuntimeException("401");
        }
        return true;
    }

    private static Integer getIndex(Employee employee, List<Employee> employees){
        Integer index=null;

        for(int i=0;i<employees.size();i++){
            if(employees.get(i).getAccount().equals(employee.getAccount())){
                index=i;
            }
        }
        return index;
    }

    private static void addToken(String token, BoundListOperations listOperations) {
        String username = JWT.decode(token).getAudience().get(0);
        Integer index = getTokenIndex(username,listOperations);

        if(index == null) {
            listOperations.leftPush(token);
        } else {
            listOperations.set(index,token);
        }

    }

    private static Integer getTokenIndex(String username, BoundListOperations listOperations) {
        List<String> tokens = listOperations.range(0,listOperations.size()-1);
        Integer index = null;

        for(int i=0;i<tokens.size();i++){
            if(username.equals(JWT.decode(tokens.get(i)).getAudience().get(0))) {
                index = i;
            }
        }

        return index;
    }

}

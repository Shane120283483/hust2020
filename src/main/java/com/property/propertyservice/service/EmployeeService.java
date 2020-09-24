package com.property.propertyservice.service;

import com.property.propertyservice.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAll();
    void addEmployee(Employee employee);
    String login(String userName,String password);
    boolean checkToken(String userName,String token);
    void batchDelete(List<String> accounts);
    void update(Employee employee);
    void logout(String username);
}

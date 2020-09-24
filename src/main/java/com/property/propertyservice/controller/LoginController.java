package com.property.propertyservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.propertyservice.entity.Employee;
import com.property.propertyservice.service.impl.EmployeeServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

class LoginInfo {
    public int return_val;
    public String token;
    public String user_name;
    public String icon_url;
    public String account;
}

@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EmployeeServiceImpl employeeService;

    @RequestMapping(value = "/login", method = RequestMethod.POST) 
    public String CheckLogin(@RequestBody Employee user) throws JsonProcessingException {

        logger.info("[user:" + user.getAccount() + "] tries logging in with [password:" + user.getPassword() + "]");
        
        LoginInfo ret = new LoginInfo();
        try {
            ret.return_val = 0;
            ret.token = employeeService.login(user.getAccount(), user.getPassword());
            List<Employee> allEmployees = employeeService.getAll();
            for (Employee i: allEmployees) {
                if (i.getAccount().equals(user.getAccount())) {
                    ret.user_name = i.getName();
                    ret.icon_url = i.getIcon();
                    if (ret.icon_url == null)
                        ret.icon_url = "img/0.svg";
                    ret.account = i.getAccount();
                    break;
                }
            }
            
            logger.info("[user:" + user.getAccount() + "] logs in successfully with [token:" + ret.token + "]");
        } catch (RuntimeException e) {
            ret.return_val = -1;
            ret.token = e.getMessage();
            
            logger.info("[user:" + user.getAccount() + "] logs in unsuccessfully with [message:" + ret.token + "]");
        }

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@RequestBody String account) {
        employeeService.logout(account);
        logger.info("[user:" + account + "] logout");
    }
}
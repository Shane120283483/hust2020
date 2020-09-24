package com.property.propertyservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.property.propertyservice.constant.Privilege;
import com.property.propertyservice.entity.ElectricityMeter;
import com.property.propertyservice.entity.Employee;
import com.property.propertyservice.entity.GasMeter;
import com.property.propertyservice.entity.House;
import com.property.propertyservice.entity.Parking;
import com.property.propertyservice.entity.ParkingRecord;
import com.property.propertyservice.entity.Price;
import com.property.propertyservice.entity.WaterMeter;
import com.property.propertyservice.service.EmployeeService;
import com.property.propertyservice.service.PriceService;
import com.property.propertyservice.service.impl.ElectricityMeterServiceImpl;
import com.property.propertyservice.service.impl.EmployeeServiceImpl;
import com.property.propertyservice.service.impl.GasMeterServiceImpl;
import com.property.propertyservice.service.impl.HouseServiceImpl;
import com.property.propertyservice.service.impl.ParkingRecordServiceImpl;
import com.property.propertyservice.service.impl.ParkingServiceImpl;
import com.property.propertyservice.service.impl.PriceServiceImpl;
import com.property.propertyservice.service.impl.WaterMeterServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HouseServiceImpl houseService;

    @Autowired
    private ParkingServiceImpl parkingService;

    @Autowired
    private ParkingRecordServiceImpl parkingRecordService;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private PriceServiceImpl priceService;

    @Autowired 
    private WaterMeterServiceImpl waterMeterService;

    @Autowired 
    private ElectricityMeterServiceImpl electricityMeterService;

    @Autowired 
    private GasMeterServiceImpl gasMeterService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String HttpGetHandler(final ServletRequest request, final HashMap<String, Object> map) {
        final String query = request.getParameter("query");
                
        final String ip = request.getRemoteAddr();
        logger.info((query != null ? query : "Index") + " page requested by IP[" + ip + "]");    

        if (query == null) return "/MainPage";
        
        final Integer queryPri = Privilege.BIT.get(query);

        final String account = request.getParameter("account");
        final String token = request.getParameter("token");

        if (query.equals("权限管理") && !account.equals("root")) {
            logger.info(query + ": Permission Denied");
            return "/PermissionDenied";
        }

        if (queryPri != null) {
            
            if (account == null || token == null) {
                logger.info(query + ": Permission Denied");
                return "/PermissionDenied";
            }

            try {
                if (!employeeService.checkToken(account, token)) {
                    logger.info(query + ": Permission Denied");
                    return "/PermissionDenied";
                }
            } catch (final RuntimeException e) {
                logger.info(query + ": Permission Denied");
                return "/PermissionDenied";
            }

            final List<Employee> allEmployee = employeeService.getAll();
            int privi = 0;
            for (final Employee i: allEmployee)
                if (i.getAccount().equals(account))
                    {privi = i.getPrivilege(); break;}
            
            if ((privi & queryPri) == 0) {
                logger.info(query + ": Permission Denied");
                return "/PermissionDenied";
            }
        }


        if (query.equals("房产管理")) {
            final List<House> allHouse = houseService.getAll();
            map.put("DataList", allHouse);
            logger.info("data size = " + Integer.toString(allHouse.size()));

        } else if (query.equals("车位使用管理")) {
            final List<Parking> allParking = parkingService.getAll();
            map.put("DataList", allParking);
            logger.info("data size = " + Integer.toString(allParking.size()));

        } else if (query.equals("停车信息管理")) {
            final List<ParkingRecord> allParkingRecord = parkingRecordService.getAll();
            map.put("DataList", allParkingRecord);
            for (final ParkingRecord i: allParkingRecord) {
                if (i.getOutDate() == null)
                    i.setOutDate("未出场");
            }

            logger.info("data size = " + Integer.toString(allParkingRecord.size()));

        } else if (query.equals("价格管理")) {
            final List<Price> allPrice = priceService.getAll();
            map.put("DataList", allPrice);
            logger.info("data size = " + Integer.toString(allPrice.size()));

        } else if (query.equals("水费管理")) {
            final List<WaterMeter> allWaterMeter = waterMeterService.getAll();
            map.put("DataList", allWaterMeter);
            logger.info("data size = " + Integer.toString(allWaterMeter.size()));
        
        } else if (query.equals("电费管理")) {
            final List<ElectricityMeter> allElectricityMeter = electricityMeterService.getAll();
            map.put("DataList", allElectricityMeter);
            logger.info("data size = " + Integer.toString(allElectricityMeter.size()));
        
        } else if (query.equals("煤气费管理")) {
            final List<GasMeter> allGasMeter = gasMeterService.getAll();
            map.put("DataList", allGasMeter);
            logger.info("data size = " + Integer.toString(allGasMeter.size()));
        
        } else if (query.equals("管理员列表")) {
            final List<Employee> allEmployee = employeeService.getAll();
            map.put("DataList", allEmployee);
            logger.info("data size = " + Integer.toString(allEmployee.size()));

        } else if (query.equals("权限管理")) {
            final List<Employee> allEmployee = employeeService.getAll();
            final List<PrivilegeMessage> ret = new ArrayList<PrivilegeMessage>();
        
            for (final Employee i: allEmployee) {
                final PrivilegeMessage tmp = new PrivilegeMessage();
                tmp.account = i.getAccount();
                tmp.privilege = new ArrayList<Boolean>();
                for (int j = 0; j < 9; ++ j) {
                    tmp.privilege.add((i.getPrivilege() & (1 << j)) != 0);
                }
                ret.add(tmp);
            }

            map.put("DataList", ret);
            logger.info("data size = " + Integer.toString(ret.size()));
        }

        return "/" + query;
    }
}

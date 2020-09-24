package com.property.propertyservice.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.propertyservice.constant.Privilege;
import com.property.propertyservice.entity.ElectricityMeter;
import com.property.propertyservice.entity.Employee;
import com.property.propertyservice.entity.GasMeter;
import com.property.propertyservice.entity.House;
import com.property.propertyservice.entity.Parking;
import com.property.propertyservice.entity.ParkingRecord;
import com.property.propertyservice.entity.Price;
import com.property.propertyservice.entity.Proprietor;
import com.property.propertyservice.entity.WaterMeter;
import com.property.propertyservice.service.EmployeeService;
import com.property.propertyservice.service.ParkingRecordService;
import com.property.propertyservice.service.ProprietorService;
import com.property.propertyservice.service.WaterMeterService;
import com.property.propertyservice.service.impl.ElectricityMeterServiceImpl;
import com.property.propertyservice.service.impl.EmployeeServiceImpl;
import com.property.propertyservice.service.impl.GasMeterServiceImpl;
import com.property.propertyservice.service.impl.HouseServiceImpl;
import com.property.propertyservice.service.impl.ParkingRecordServiceImpl;
import com.property.propertyservice.service.impl.ParkingServiceImpl;
import com.property.propertyservice.service.impl.PriceServiceImpl;
import com.property.propertyservice.service.impl.ProprietorServiceImpl;
import com.property.propertyservice.service.impl.WaterMeterServiceImpl;

import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class OperationResponse {
    public int return_val;
    public String info;
}

@RestController
public class OperationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HouseServiceImpl houseService;

    @Autowired
    private ParkingServiceImpl parkingService;

    @Autowired
    private EmployeeServiceImpl employService;

    @Autowired
    private PriceServiceImpl priceService;

    @Autowired
    private ParkingRecordServiceImpl parkingRecordService;

    @Autowired
    private WaterMeterServiceImpl waterMeterService;

    @Autowired
    private ElectricityMeterServiceImpl electricityMeterService;

    @Autowired
    private GasMeterServiceImpl gasMeterService;

    @Autowired
    private ProprietorServiceImpl proprietorService;

    @RequestMapping(value = "/房产管理/add", method = RequestMethod.POST)
    public String HouseServiceAddHandler(@RequestBody House house, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("房产管理 add operation called from [user:" + account + "], address = " + house.getAddress()
                + ", area = " + Float.toString(house.getArea()) + ", owner = " + house.getOwner());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("房产管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    house.setAccount(0);
                    houseService.addHouse(house);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("房产管理 add operation from [user:" + account + "] done");
        else
            logger.info("房产管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/房产管理/delete", method = RequestMethod.POST)
    public String HouseServiceDeleteHandler(@RequestBody List<Integer> house, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info(
                "房产管理 delete operation called from [user:" + account + "], size = " + Integer.toString(house.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("房产管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    houseService.batchDelete(house);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("房产管理 delete operation from [user:" + account + "] done");
        else
            logger.info("房产管理 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/车位使用管理/add", method = RequestMethod.POST)
    public String ParkingServiceAddHandler(@RequestBody Parking parking, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("车位使用管理 add operation called from [user:" + account + "], carid = " + parking.getCarId()
                + ", owner = " + parking.getOwner() + ", address = " + parking.getAddress());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("车位使用管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    parkingService.addParking(parking);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("车位使用管理 add operation from [user:" + account + "] done");
        else
            logger.info("车位使用管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/车位使用管理/delete", method = RequestMethod.POST)
    public String ParkingServiceDeleteHandler(@RequestBody List<Integer> parking,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("车位使用管理 delete operation called from [user:" + account + "], size = "
                + Integer.toString(parking.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("车位使用管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {

            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    parkingService.batchDelete(parking);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("车位使用管理 delete operation from [user:" + account + "] done");
        else
            logger.info("车位使用管理 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/停车信息管理/add", method = RequestMethod.POST)
    public String ParkingRecordServiceAddHandler(@RequestBody ParkingRecord parking,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("停车信息管理 add operation called from [user:" + account + "], carid = " + parking.getCarId()
                + ", entry date = " + parking.getEntryDate());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("停车信息管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    parkingRecordService.addParkingRecord(parking);
                    logger.info("[carId:" + parking.getCarId() + "] 停车入场");
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("停车信息管理 add operation from [user:" + account + "] done");
        else
            logger.info("停车信息管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/停车信息管理/finish", method = RequestMethod.POST)
    public String ParkingRecordServiceFinishHandler(@RequestBody ParkingRecord parking,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("停车信息管理 finish operation called from [user:" + account + "], carid = " + parking.getCarId()
                + ", out date = " + parking.getEntryDate());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("停车信息管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    List<ParkingRecord> allRecord = parkingRecordService.getAll();
                    for (ParkingRecord i : allRecord) {

                        if (i.getId() == parking.getId()) {

                            if (i.getOutDate() != null) {
                                ret.info = "已出场，无需再次支付";
                            } else {
                                float price = 0;
                                List<Price> pri = priceService.getAll();
                                if (pri.size() != 0)
                                    price = pri.get(0).getParking();
                                LocalDateTime start = LocalDateTime.parse(parking.getEntryDate().replace(' ', 'T'));
                                LocalDateTime end = LocalDateTime.parse(parking.getOutDate().replace(' ', 'T'));
                                double total = start.until(end, ChronoUnit.HOURS) * price;

                                ret.info = "需支付" + String.format("%.2f", total) + "元";
                                logger.info("[carId:" + parking.getCarId() + "] 停车出场, [time(seconds):"
                                        + Long.toString(start.until(end, ChronoUnit.SECONDS)) + "]");
                            }

                            break;
                        }
                    }

                    parkingRecordService.update(parking);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("停车信息管理 finish operation from [user:" + account + "] done");
        else
            logger.info("停车信息管理 finish operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/停车信息管理/delete", method = RequestMethod.POST)
    public String ParkingRecordServiceDeleteHandler(@RequestBody List<Integer> parking,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("停车信息管理 delete operation called from [user:" + account + "], size = "
                + Integer.toString(parking.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("停车信息管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {

            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    parkingRecordService.batchDelete(parking);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("停车信息管理 delete operation from [user:" + account + "] done");
        else
            logger.info("停车信息管理 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/价格管理/add", method = RequestMethod.POST)
    public String PriveServiceAddHandler(@RequestBody Price price, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("价格管理 add operation called from [user:" + account + "], data = "
                + (new ObjectMapper()).writeValueAsString(price));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("价格管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    try {
                        priceService.update(price);
                    } catch (RuntimeException e) {
                        priceService.addPrice(price);
                    }
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("价格管理 add operation from [user:" + account + "] done");
        else
            logger.info("价格管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/水费管理/add", method = RequestMethod.POST)
    public String WaterServiceAddHandler(@RequestBody WaterMeter water, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("水费管理 add operation called from [user:" + account + "], data = "
                + Double.toString(water.getCurrentData()) + ", date = " + water.getDate() + ", house = "
                + water.getHouseId());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("水费管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    waterMeterService.addWaterMeter(water);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("水费管理 add operation from [user:" + account + "] done");
        else
            logger.info("水费管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/水费管理/delete", method = RequestMethod.POST)
    public String WaterServiceDeleteHandler(@RequestBody List<Integer> water, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info(
                "水费管理 delete operation called from [user:" + account + "], size = " + Integer.toString(water.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("水费管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    waterMeterService.batchDelete(water);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("水费管理 delete operation from [user:" + account + "] done");
        else
            logger.info("水费管理 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/电费管理/add", method = RequestMethod.POST)
    public String ElectricityServiceAddHandler(@RequestBody ElectricityMeter electricity,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("电费管理 add operation called from [user:" + account + "], data = "
                + Double.toString(electricity.getCurrentData()) + ", date = " + electricity.getDate() + ", house = "
                + electricity.getHouseId());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("电费管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    electricityMeterService.addElectricityMeter(electricity);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("电费管理 add operation from [user:" + account + "] done");
        else
            logger.info("电费管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/电费管理/delete", method = RequestMethod.POST)
    public String ElectricityServiceDeleteHandler(@RequestBody List<Integer> electricity,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("电费管理 delete operation called from [user:" + account + "], size = "
                + Integer.toString(electricity.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("电费管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    electricityMeterService.batchDelete(electricity);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("电费管理 delete operation from [user:" + account + "] done");
        else
            logger.info("电费管理 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/煤气费管理/add", method = RequestMethod.POST)
    public String GasServiceAddHandler(@RequestBody GasMeter gas, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("煤气费管理 add operation called from [user:" + account + "], data = "
                + Double.toString(gas.getCurrentData()) + ", date = " + gas.getDate() + ", house = "
                + gas.getHouseId());

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("煤气费管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    gasMeterService.addGasMeter(gas);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("煤气费管理 add operation from [user:" + account + "] done");
        else
            logger.info("煤气费管理 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/煤气费管理/delete", method = RequestMethod.POST)
    public String GasServiceDeleteHandler(@RequestBody List<Integer> gas, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info(
                "煤气费管理 delete operation called from [user:" + account + "], size = " + Integer.toString(gas.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("煤气费管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    gasMeterService.batchDelete(gas);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("煤气费管理 delete operation from [user:" + account + "] done");
        else
            logger.info("煤气费管理 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/权限管理/update", method = RequestMethod.POST)
    public String PriviligeServiceDeleteHandler(@RequestBody PrivilegeMessage employee,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("权限管理 update operation called from [user:" + account + "], target user = " + employee.account);

        OperationResponse ret = new OperationResponse();

        if (!account.equals("root")) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    List<Employee> allEmployee = employService.getAll();
                    for (Employee i : allEmployee) {
                        if (employee.account.equals(i.getAccount())) {

                            Employee tmp = i;
                            int newPri = 0;
                            for (int j = 0; j < 9; ++j) {
                                if (employee.privilege.get(j) == true)
                                    newPri |= (1 << j);
                            }
                            logger.info("new privilege: " + Integer.toBinaryString(newPri));
                            tmp.setPrivilege(newPri);
                            employService.update(tmp);
                            break;
                        }
                    }

                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("权限管理 update operation from [user:" + account + "] done");
        else
            logger.info("权限管理 update operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/管理员列表/add", method = RequestMethod.POST)
    public String EmployeeServiceAddHandler(@RequestBody Employee employee, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("管理员列表 add operation called from [user:" + account + "], data = "
                + (new ObjectMapper()).writeValueAsString(employee));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("管理员列表")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    employee.setPassword("e10adc3949ba59abbe56e057f20f883e");
                    employee.setPrivilege(0);
                    employService.addEmployee(employee);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("管理员列表 add operation from [user:" + account + "] done");
        else
            logger.info("管理员列表 add operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/管理员列表/delete", method = RequestMethod.POST)
    public String EmployeeServiceDeleteHandler(@RequestBody List<String> employee,
            @RequestParam("account") String account, @RequestParam("token") String token) throws IOException {

        logger.info("管理员列表 delete operation called from [user:" + account + "], size = "
                + Integer.toString(employee.size()));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("管理员列表")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    employService.batchDelete(employee);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("管理员列表 delete operation from [user:" + account + "] done");
        else
            logger.info("管理员列表 delete operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/收费明细管理/get", method = RequestMethod.GET)
    public String ChargeServiceAddHandler(@RequestParam("date") String date, @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("收费明细管理 get operation called from [user:" + account + "], date = " + date);

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("收费明细管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;

                    List<ChargeMessage> tmp = new ArrayList<ChargeMessage>();
                    List<House> allHouse = houseService.getAll();
                    List<ElectricityMeter> allEle = electricityMeterService.getAll();
                    List<WaterMeter> allWater = waterMeterService.getAll();
                    List<GasMeter> allGas = gasMeterService.getAll();
                    List<Proprietor> allRec = proprietorService.getAll();
                    List<Price> allPri = priceService.getAll();

                    float eleFee = allPri.size() != 0 ? allPri.get(0).getElectricity() : 0;
                    float waterFee = allPri.size() != 0 ? allPri.get(0).getWater() : 0;
                    float gasFee = allPri.size() != 0 ? allPri.get(0).getGas() : 0;

                    for (House i : allHouse) {
                        ChargeMessage newMessage = new ChargeMessage();
                        newMessage.id = i.getId();
                        newMessage.address = i.getAddress();

                        boolean flag = false;
                        for (Proprietor j : allRec) {
                            if (j.getIdCard().equals(Integer.toString(newMessage.id) + " " + date)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag != true) {
                            Proprietor newPro = new Proprietor();
                            newPro.setDone(false);
                            newPro.setEleFee(-1);
                            newPro.setGasFee(-1);
                            newPro.setWaterFee(-1);
                            newPro.setIdCard(Integer.toString(newMessage.id) + " " + date);
                            proprietorService.addProprietor(newPro);
                            allRec = proprietorService.getAll();
                        }

                        for (Proprietor j : allRec) {
                            if (j.getIdCard().equals(Integer.toString(newMessage.id) + " " + date)) {

                                if (j.getEleFee() == -1) {
                                    float frontVal = -1;
                                    float nowVal = -1;
                                    LocalDate target = LocalDate.parse(date);
                                    LocalDate front = target.minusMonths(1);
                                    for (ElectricityMeter k : allEle) {
                                        LocalDate rec = LocalDate.parse(k.getDate());
                                        if (rec.getMonth() == front.getMonth() && rec.getYear() == front.getYear() && k.getHouseId() == newMessage.id) {
                                            frontVal = k.getCurrentData();
                                            break;
                                        }
                                    }
                                    for (ElectricityMeter k : allEle) {
                                        LocalDate rec = LocalDate.parse(k.getDate());
                                        if (rec.getMonth() == target.getMonth() && rec.getYear() == target.getYear() && k.getHouseId() == newMessage.id) {
                                            nowVal = k.getCurrentData();
                                            break;
                                        }
                                    }
                                    if (frontVal != -1 && nowVal != -1) {
                                        j.setEleFee((nowVal - frontVal) * eleFee);
                                        proprietorService.update(j);
                                    }
                                }

                                if (j.getWaterFee() == -1) {
                                    float frontVal = -1;
                                    float nowVal = -1;
                                    LocalDate target = LocalDate.parse(date);
                                    LocalDate front = target.minusMonths(1);
                                    for (WaterMeter k : allWater) {
                                        LocalDate rec = LocalDate.parse(k.getDate());
                                        if (rec.getMonth() == front.getMonth() && rec.getYear() == front.getYear() && k.getHouseId() == newMessage.id) {
                                            frontVal = k.getCurrentData();
                                            break;
                                        }
                                    }
                                    for (WaterMeter k : allWater) {
                                        LocalDate rec = LocalDate.parse(k.getDate());
                                        if (rec.getMonth() == target.getMonth() && rec.getYear() == target.getYear() && k.getHouseId() == newMessage.id) {
                                            nowVal = k.getCurrentData();
                                            break;
                                        }
                                    }
                                    if (frontVal != -1 && nowVal != -1) {
                                        j.setWaterFee((nowVal - frontVal) * waterFee);
                                        proprietorService.update(j);
                                    }
                                }

                                if (j.getGasFee() == -1) {
                                    float frontVal = -1;
                                    float nowVal = -1;
                                    LocalDate target = LocalDate.parse(date);
                                    LocalDate front = target.minusMonths(1);
                                    for (GasMeter k : allGas) {
                                        LocalDate rec = LocalDate.parse(k.getDate());
                                        if (rec.getMonth() == front.getMonth() && rec.getYear() == front.getYear() && k.getHouseId() == newMessage.id) {
                                            frontVal = k.getCurrentData();
                                            break;
                                        }
                                    }
                                    for (GasMeter k : allGas) {
                                        LocalDate rec = LocalDate.parse(k.getDate());
                                        if (rec.getMonth() == target.getMonth() && rec.getYear() == target.getYear() && k.getHouseId() == newMessage.id) {
                                            nowVal = k.getCurrentData();
                                            break;
                                        }
                                    }
                                    if (frontVal != -1 && nowVal != -1) {
                                        j.setGasFee((nowVal - frontVal) * gasFee);
                                        proprietorService.update(j);
                                    }
                                }

                                newMessage.electricityFee = j.getEleFee() != -1 ? String.format("%.2f", j.getEleFee())
                                        : "读数缺失";
                                newMessage.waterFee = j.getWaterFee() != -1 ? String.format("%.2f", j.getWaterFee())
                                        : "读数缺失";
                                newMessage.gasFee = j.getGasFee() != -1 ? String.format("%.2f", j.getGasFee()) : "读数缺失";

                                newMessage.money = j.getEleFee() >= 0 && j.getWaterFee() >= 0 && j.getGasFee() >= 0
                                        ? String.format("%.2f", j.getEleFee() + j.getWaterFee() + j.getGasFee())
                                        : "读数缺失";

                                if (j.isDone())
                                    newMessage.operation = "已完成";
                                else if (!newMessage.money.equals("读数缺失") && j.getEleFee() + j.getWaterFee() + j.getGasFee() > i.getAccount())
                                    newMessage.operation = "余额不足";
                                else if (!newMessage.money.equals("读数缺失"))
                                    newMessage.operation = "支付";
                                else 
                                    newMessage.operation = "无";
                                break;
                            }
                        }

                        tmp.add(newMessage);
                    }

                    ret.info = (new ObjectMapper()).writeValueAsString(tmp);
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("收费明细管理 get operation from [user:" + account + "] done");
        else
            logger.info("收费明细管理 get operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }


    @RequestMapping(value = "/房产账户管理/update", method = RequestMethod.POST)
    public String HouseAccountServiceAddHandler(
            @RequestBody AccountMessage accountMessage, 
            @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("房产账户管理 update operation called from [user:" + account + "], target = " + accountMessage.target
                + ", value = " + Float.toString(accountMessage.change));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("房产管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    List<House> allHouse = houseService.getAll();
                    for (House i: allHouse) {
                        if (i.getId() == accountMessage.target) {
                            i.setAccount(i.getAccount() + accountMessage.change);
                            houseService.update(i);
                            break;
                        }
                    }
                    
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("房产账户管理 update operation from [user:" + account + "] done");
        else
            logger.info("房产账户管理 update operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }

    @RequestMapping(value = "/房产账户管理/pay", method = RequestMethod.POST)
    public String HouseAccountPpayServiceAddHandler(
            @RequestBody PayMessage accountMessage, 
            @RequestParam("account") String account,
            @RequestParam("token") String token) throws IOException {

        logger.info("房产账户管理 pay operation called from [user:" + account + "], target = " + accountMessage.key
                + ", value = " + Float.toString(accountMessage.change));

        OperationResponse ret = new OperationResponse();

        Integer privi = 0;
        List<Employee> allEmployee = employService.getAll();
        for (Employee i : allEmployee) {
            if (i.getAccount().equals(account)) {
                privi = i.getPrivilege();
                break;
            }
        }
        if ((privi & Privilege.BIT.get("收费明细管理")) == 0) {
            ret.return_val = -1;
            ret.info = "Permission Denied";
        } else {
            try {
                boolean result = employService.checkToken(account, token);
                if (result == true) {
                    ret.return_val = 0;
                    List<House> allHouse = houseService.getAll();
                    for (House i: allHouse) {
                        if (i.getId() == accountMessage.target) {
                            i.setAccount(i.getAccount() - accountMessage.change);
                            houseService.update(i);
                            break;
                        }
                    }

                    List<Proprietor> allRec = proprietorService.getAll();
                    for (Proprietor i: allRec) {
                        if (i.getIdCard().equals(accountMessage.key)) {
                            i.setDone(true);
                            proprietorService.update(i);
                            break;
                        }
                    }
                    
                } else {
                    ret.return_val = -1;
                    ret.info = "token错误，请联系管理员";
                }
            } catch (RuntimeException e) {
                ret.return_val = -1;
                ret.info = e.getMessage();
            }
        }

        if (ret.return_val == 0)
            logger.info("房产账户管理 pay operation from [user:" + account + "] done");
        else
            logger.info("房产账户管理 pay operation from [user:" + account + "] fail due to [message:" + ret.info + "]");

        return (new ObjectMapper()).writeValueAsString(ret);
    }
}

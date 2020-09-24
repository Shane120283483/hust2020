package com.property.propertyservice.entity;

/**
 *
 * 员工模型类
 */
public class Employee {
    private String account;
    private String password;
    private String name;
    private String birthday;
    private String employDate;
    private String sex;
    private String phone;
    private String title;
    private String icon;
    private int privilege;

    public String getAccount() {
        return account;
    }
        
    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmployDate() {
        return employDate;
    }

    public void setEmployDate(String employDate) {
        this.employDate = employDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String newIcon) {
        icon = newIcon;
    }
}

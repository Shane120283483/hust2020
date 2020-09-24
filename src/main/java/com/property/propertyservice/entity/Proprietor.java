package com.property.propertyservice.entity;

/**
 *
 * 业主模型类
 */
public class Proprietor {
    private String idCard;
    private float eleFee;
    private float waterFee;
    private float gasFee;
    private boolean done;

    public String getIdCard() {
        return idCard;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public float getGasFee() {
        return gasFee;
    }

    public void setGasFee(float gasFee) {
        this.gasFee = gasFee;
    }

    public float getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(float waterFee) {
        this.waterFee = waterFee;
    }

    public float getEleFee() {
        return eleFee;
    }

    public void setEleFee(float eleFee) {
        this.eleFee = eleFee;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }



}

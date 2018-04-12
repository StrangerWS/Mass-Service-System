package com.strangerws.ssu.mss.model.element;

public class Requirement {
    private double arriveTime;
    private double serviceTime;
    private double exitTime;
    private boolean declined;

    public double getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(double arriveTime) {
        this.arriveTime = arriveTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getExitTime() {
        return exitTime;
    }

    public void setExitTime(double exitTime) {
        this.exitTime = exitTime;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void decline() {
        this.declined = true;
        serviceTime = Double.MAX_VALUE;
        exitTime = Double.MAX_VALUE;
    }

    public Requirement() {
    }
}

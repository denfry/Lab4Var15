package com.example.lab4var15;

public class PeripheralDevice {
    private String name;
    private double price;
    private String deviceType;

    public PeripheralDevice(String name, double price, String deviceType) {
        this.name = name;
        this.price = price;
        this.deviceType = deviceType;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getDeviceType() {
        return deviceType;
    }
}

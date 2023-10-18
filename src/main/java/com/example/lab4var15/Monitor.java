package com.example.lab4var15;

public class Monitor extends PeripheralDevice {
    private int screenSize;
    public Monitor(String name, double price, int screenSize, String deviceType) {
        super(name, price, deviceType);
        this.screenSize = screenSize;
    }
    public int getScreenSize() {
        return screenSize;
    }
}

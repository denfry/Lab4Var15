package com.example.lab4var15;

public class Mouse extends PeripheralDevice {

    private boolean wireless;
    public Mouse(String name, double price, boolean wireless, String deviceType) {
        super(name, price, deviceType);
        this.wireless = wireless;
    }
    public boolean isWireless() {
        return wireless;
    }
}

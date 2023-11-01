package com.example.lab4var15;

import javafx.beans.property.SimpleBooleanProperty;

public class Mouse extends PeripheralDevice {

    private boolean wireless;
    public Mouse(String name, double price, boolean wireless, String deviceType) {
        super(name, price, deviceType);
        this.wireless = wireless;
    }
    public boolean getWireless() {
        return wireless;
    }
}

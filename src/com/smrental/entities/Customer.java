package com.smrental.entities;

public class Customer {
    public enum CustomerStatus {
        BOARDING,
        UNBOARDING,
        WAITING_PICKUP,
        WAITING_SERVICING,
        SERVING
    }

    public enum CustomerType {
        CHECK_IN,
        CHECK_OUT
    }

    public CustomerType uType;
    public double timeEnterSystem;
    public int numberOfAdditionalPassenager;
    public CustomerStatus customerStatus;

    @Override
    public String toString() {
        return String.format("[type: %s, add_pass:%s]", uType.name(), numberOfAdditionalPassenager);
    }
}

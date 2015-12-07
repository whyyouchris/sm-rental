package com.smrental.entities;

public enum VanType {
    SEAT12(12),
    SEAT18(18),
    SEAT30(30);

    private int seats;

    private VanType(int seats) {
        this.seats = seats;
    }

    public int getSeats() {
        return this.seats;
    }
}

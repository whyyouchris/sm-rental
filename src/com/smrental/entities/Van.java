package com.smrental.entities;

import java.util.ArrayList;
import java.util.List;

public class Van {
    // The id attribute is only for the purpose of toString method
    public final int id;
    public final int capacity;
    public final List<Customer> onBoardCustomers;
    public int numOfSeatTaken;
    public VanStatus status;

    public Van(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.onBoardCustomers = new ArrayList<>(capacity);
        this.numOfSeatTaken = 0;
        this.status = VanStatus.IDLE;
    }

    @Override
    public String toString() {
        return String.format("[id: %s numSeatTaken: %s]", id, numOfSeatTaken);
    }

    public enum VanStatus {
        LOADING,
        UNLOADING,
        IDLE,
        DRIVING_COUNTER_T1,
        DRIVING_COUNTER_DROP_OFF,
        DRIVING_DROP_OFF_T1,
        DRIVING_T1_T2,
        DRIVING_T2_COUNTER
    }

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
}

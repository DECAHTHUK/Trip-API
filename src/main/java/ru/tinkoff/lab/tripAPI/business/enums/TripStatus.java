package ru.tinkoff.lab.tripAPI.business.enums;

public enum TripStatus {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    String status;

    TripStatus(String status) {
        this.status = status;
    }

    void setStatus(String status) {
        this.status = status;
    }
}

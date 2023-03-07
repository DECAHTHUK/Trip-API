package ru.tinkoff.lab.tripAPI.business.enums;

public enum TripStatus {
    PENDING,
    COMPLETED,
    CANCELLED;

    String status;

    TripStatus(String status) {
        this.status = status;
    }

    TripStatus() {

    }

    void setStatus(String status) {
        this.status = status;
    }
}

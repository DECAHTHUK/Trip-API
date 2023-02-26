package ru.tinkoff.lab.tripAPI.business.enums;

public enum RequestStatus {
    APPROVED("APPROVED"),
    PENDING("PENDING"),
    AWAIT_CHANGES("AWAIT_CHANGES"),
    DECLINED("DECLINED");

    String status;

    RequestStatus(String status) {
        this.status = status;
    }

    void setStatus(String status) {
        this.status = status;
    }
}

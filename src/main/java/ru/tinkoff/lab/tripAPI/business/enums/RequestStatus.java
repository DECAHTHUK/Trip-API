package ru.tinkoff.lab.tripAPI.business.enums;

public enum RequestStatus {
    APPROVED,
    PENDING,
    AWAIT_CHANGES,
    DECLINED;

    String status;

    RequestStatus(String status) {
        this.status = status;
    }

    RequestStatus() {

    }

    void setStatus(String status) {
        this.status = status;
    }
}

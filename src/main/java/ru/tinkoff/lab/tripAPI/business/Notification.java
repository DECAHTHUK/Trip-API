package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;

@Data
public class Notification {
    private String id;

    private Request request;

    private boolean watched;

    private String userId;

    public Notification() {
        this.watched = false;
    }
}

package ru.tinkoff.lab.tripAPI.business.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String id;

    private String requestId;

    private boolean watched;

    private String userId;

    public NotificationDto() {
        this.watched = false;
    }
}

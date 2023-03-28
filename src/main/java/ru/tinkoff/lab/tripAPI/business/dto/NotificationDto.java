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

    public NotificationDto(String requestId, boolean watched, String userId) {
        this.requestId = requestId;
        this.watched = watched;
        this.userId = userId;
    }
}

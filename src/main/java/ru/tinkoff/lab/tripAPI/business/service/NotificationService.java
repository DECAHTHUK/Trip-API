package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Notification;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;
import ru.tinkoff.lab.tripAPI.exceptions.NotificationCreateException;
import ru.tinkoff.lab.tripAPI.exceptions.NotificationNotFoundException;
import ru.tinkoff.lab.tripAPI.mapping.NotificationMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public Id createNotification(NotificationDto notificationDto) {
        try {
            return notificationMapper.insertNotification(notificationDto);
        } catch (NotificationCreateException e) {
            throw new NotificationCreateException(e.getMessage());
        }
    }

    public void createMultipleNotifications(List<NotificationDto> notifications) {
        try {
            notificationMapper.insertMultipleNotifications(notifications);
        } catch (NotificationCreateException e) {
            throw new NotificationCreateException(e.getMessage());
        }
    }

    public Notification getNotificationById(UUID uuid) {
        Notification notification = notificationMapper.selectNotification(uuid);
        if (notification == null) {
            throw new NotificationNotFoundException("Notification with id = " + uuid + " was not found");
        }
        return notification;
    }

    public void markNotificationAsWatched(UUID uuid) {
        notificationMapper.updateNotificationToWatched(uuid);
    }

    public List<Notification> getUnwatchedNotifications(UUID uuid) {
        return notificationMapper.selectUnwatchedNotifications(uuid);
    }

    public void updateNotification(NotificationDto notificationDto) {
        notificationMapper.updateNotification(notificationDto);
    }

    public void deleteNotification(UUID uuid) {
        notificationMapper.deleteNotification(uuid);
    }
}

package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Notification;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;
import ru.tinkoff.lab.tripAPI.mapping.NotificationMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public Id createNotification(NotificationDto notificationDto) {
        return notificationMapper.insertNotification(notificationDto);
    }

    public Notification getNotificationById(UUID uuid) {
        Notification notification = notificationMapper.selectNotification(uuid);
        if (notification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification with id = " + uuid + " was not found");
        }
        return notification;
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

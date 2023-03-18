package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Notification;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;
import ru.tinkoff.lab.tripAPI.business.service.NotificationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PutMapping("/{uuid}/watch")
    public void markAsWatched(@PathVariable UUID uuid) {
        notificationService.markNotificationAsWatched(uuid);
    }

    @PostMapping("")
    public Id createNotification(@RequestBody NotificationDto notificationDto) {
        return notificationService.createNotification(notificationDto);
    }

    @GetMapping("/{uuid}")
    public Notification getNotification(@PathVariable UUID uuid) {
        return notificationService.getNotificationById(uuid);
    }
}

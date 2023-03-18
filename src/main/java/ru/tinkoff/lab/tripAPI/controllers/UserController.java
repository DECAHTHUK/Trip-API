package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.service.AccommodationDestinationTripService;
import ru.tinkoff.lab.tripAPI.business.service.NotificationService;
import ru.tinkoff.lab.tripAPI.business.service.RequestService;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final RequestService requestService;

    private final NotificationService notificationService;

    private final AccommodationDestinationTripService accommodationDestinationTripService;

    @PostMapping("")
    public Id createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{uuid}")
    public User getUserById(@PathVariable UUID uuid) {
        return userService.findById(uuid);
    }

    @PutMapping("")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping("/{uuid}")
    public void deleteUserById(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }

    @PostMapping("/{approver}/subordinates/{subordinate}")
    public void createRelation(@PathVariable UUID approver, @PathVariable UUID subordinate) {
        userService.createRelation(approver, subordinate);
    }

    @DeleteMapping("/{approver}/subordinates/{subordinate}")
    public void deleteRelation(@PathVariable UUID approver, @PathVariable UUID subordinate) {
        userService.deleteRelation(approver, subordinate);
    }

    @GetMapping("/{uuid}/incoming-requests-at/{page}")
    public List<Request> getIncomingRequests(@PathVariable UUID uuid, @PathVariable int page) {
        return requestService.getIncomingRequests(uuid, page);
    }

    @GetMapping("/{uuid}/outgoing-requests-at/{page}")
    public List<Request> getOutgoingRequests(@PathVariable UUID uuid, @PathVariable int page) {
        return requestService.getOutgoingRequests(uuid, page);
    }

    @GetMapping("/{uuid}/trips-at/{page}")
    public List<Trip> getSomeTrips(@PathVariable UUID uuid, @PathVariable int page) {
        return accommodationDestinationTripService.getSomeTrips(uuid, page);
    }

    @GetMapping("/{uuid}/unwatched-notifications")
    public List<Notification> getUnwatchedNotifications(@PathVariable UUID uuid) {
        return notificationService.getUnwatchedNotifications(uuid);
    }
}
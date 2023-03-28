package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public Id createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{uuid}")
    public User getUserById(@PathVariable UUID uuid) {
        return userService.findById(uuid);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{uuid}")
    public void deleteUserById(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{approver}/subordinates/{subordinate}")
    public void createRelation(@PathVariable UUID approver, @PathVariable UUID subordinate) {
        userService.createRelation(approver, subordinate);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{approver}/subordinates/{subordinate}")
    public void deleteRelation(@PathVariable UUID approver, @PathVariable UUID subordinate) {
        userService.deleteRelation(approver, subordinate);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{uuid}/incoming-requests-at/{page}")
    public List<Request> getIncomingRequests(@PathVariable UUID uuid, @PathVariable int page) {
        return requestService.getIncomingRequests(uuid, page);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{uuid}/outgoing-requests-at/{page}")
    public List<Request> getOutgoingRequests(@PathVariable UUID uuid, @PathVariable int page) {
        return requestService.getOutgoingRequests(uuid, page);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{uuid}/trips-at/{page}")
    public List<Trip> getSomeTrips(@PathVariable UUID uuid, @PathVariable int page) {
        return accommodationDestinationTripService.getSomeTrips(uuid, page);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{uuid}/unwatched-notifications")
    public List<Notification> getUnwatchedNotifications(@PathVariable UUID uuid) {
        return notificationService.getUnwatchedNotifications(uuid);
    }
}
package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.AccommodationDestinationTripService;
import ru.tinkoff.lab.tripAPI.business.service.RequestService;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final RequestService requestService;

    private final AccommodationDestinationTripService accommodationDestinationTripService;

    @PostMapping("")
    public Id createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{uuid}")
    public User getUserById(@PathVariable String uuid) {
        return userService.findById(uuid);
    }

    @PutMapping("")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping("/{uuid}")
    public void deleteUserById(@PathVariable String uuid) {
        userService.deleteUser(uuid);
    }

    @PostMapping("/{boss}/subordinates/{subordinate}")
    public void createRelation(@PathVariable String boss, @PathVariable String subordinate) {
        userService.createRelation(boss, subordinate);
    }

    @DeleteMapping("/{boss}/subordinates/{subordinate}")
    public void deleteRelation(@PathVariable String boss, @PathVariable String subordinate) {
        userService.deleteRelation(boss, subordinate);
    }

    @GetMapping("/{uuid}/incoming-requests-at/{page}")
    public List<Request> getIncomingRequests(@PathVariable String uuid, @PathVariable int page) {
        return requestService.getIncomingRequests(uuid, page);
    }

    @GetMapping("/{uuid}/outgoing-requests-at/{page}")
    public List<Request> getOutgoingRequests(@PathVariable String uuid, @PathVariable int page) {
        return requestService.getOutgoingRequests(uuid, page);
    }

    @GetMapping("/{uuid}/trips-at/{page}")
    public List<Trip> getSomeTrips(@PathVariable String uuid, @PathVariable int page) {
        return accommodationDestinationTripService.getSomeTrips(uuid, page);
    }
}
package ru.tinkoff.lab.tripAPI.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;

import java.util.List;

@RestController
public class TestController {
    private final UserService userService;

    private final AccommodationDestinationTripMapper accommodationDestinationTripMapper;

    public TestController(UserService userService, AccommodationDestinationTripMapper accommodationDestinationTripMapper) {
        this.userService = userService;
        this.accommodationDestinationTripMapper = accommodationDestinationTripMapper;
    }

    @GetMapping("/user")
    public Id getSomeUser() {
        return userService.createUser(new User("1231", "test", "lolo", "blyaka", "sosuke"));
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/update")
    public String update() {
        userService.updateUser();
        return "Success.";
    }

    @GetMapping("/trip/{id}")
    public Trip getTrip(@PathVariable String id) {
        return accommodationDestinationTripMapper.selectTrip(id);
    }
}

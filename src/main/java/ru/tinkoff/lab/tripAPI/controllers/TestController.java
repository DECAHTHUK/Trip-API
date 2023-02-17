package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;


@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserService userService;

    private final AccommodationDestinationTripMapper accommodationDestinationTripMapper;

    @GetMapping("/user")
    public Id getSomeUser() {
        return userService.createUser(new User("1231", "test", "lolo", "blyaka", "sosuke"));
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

}

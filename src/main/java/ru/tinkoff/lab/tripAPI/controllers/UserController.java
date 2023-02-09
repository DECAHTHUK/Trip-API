package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    protected UserService userService;

    @PostMapping(value = "/user/new", produces = "application/json", consumes = "application/json")
    public Id createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(value = "/user/{uuid}/get", produces = "application/json")
    public User getUserById(@PathVariable String uuid) {
        return userService.getUser(uuid);
    }

    @PutMapping(value = "/user/new", consumes = "application/json", produces = "application/json")
    public String updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return "Success";
    }
}

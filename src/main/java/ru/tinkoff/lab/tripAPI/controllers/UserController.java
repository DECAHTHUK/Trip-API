package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserRelationService;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRelationService userRelationService;

    @PostMapping(value = "/users")
    public Id createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(value = "/users/{uuid}/get")
    public User getUserById(@PathVariable String uuid) {
        return userService.getUser(uuid);
    }

    @PutMapping(value = "/users")
    public String updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return "Success";
    }

    @PostMapping(value = "/users/{boss}/subordinates/{subordinate}")
    public void createRelation(@PathVariable String boss, @PathVariable String subordinate) {
        userRelationService.createRelation(boss, subordinate);
    }

    @DeleteMapping(value = "/users/{boss}/subordinates/{subordinate}")
    public void deleteRelation(@PathVariable String boss, @PathVariable String subordinate) {
        userRelationService.deleteRelation(boss, subordinate);
    }
}

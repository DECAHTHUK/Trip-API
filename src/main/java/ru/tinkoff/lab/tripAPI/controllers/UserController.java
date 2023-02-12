package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserRelationService;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    private UserRelationService userRelationService;

    @PostMapping(value = "/user/new", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Id createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(value = "/user/{uuid}/get", produces = "application/json")
    public User getUserById(@PathVariable String uuid) {
        return userService.getUser(uuid);
    }

    @PutMapping(value = "/user/new", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return "Success";
    }

    //TODO ask about this endpoint
    @PostMapping(value = "/user/relations/{boss}/{subordinate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createRelation(@PathVariable String boss, @PathVariable String subordinate) {
        userRelationService.createRelation(boss, subordinate);
    }

    @DeleteMapping(value = "/user/relations/{boss}/{subordinate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteRelation(@PathVariable String boss, @PathVariable String subordinate) {
        userRelationService.deleteRelation(boss, subordinate);
    }
}

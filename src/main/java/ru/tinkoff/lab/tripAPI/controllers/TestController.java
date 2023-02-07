package ru.tinkoff.lab.tripAPI.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

import java.util.List;

@RestController
public class TestController {
    private final UserService userService;
    public TestController(UserService userService) {
        this.userService = userService;
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
}

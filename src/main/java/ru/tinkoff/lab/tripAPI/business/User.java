package ru.tinkoff.lab.tripAPI.business;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private String id;

    private String email;

    private String password;

    private String firstName;

    private String secondName;

    private String userRole;

    private List<User> subordinates;

    public User(String email, String password, String firstName, String secondName, String userRole) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userRole = userRole;
    }
}

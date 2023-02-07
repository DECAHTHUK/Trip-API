package ru.tinkoff.lab.tripAPI.business;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private String id;

    private String email;

    private String password;

    private String first_name;

    private String second_name;

    private String user_role;

    private List<User> subordinates;

    public User(String email, String password, String first_name, String second_name, String user_role) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.second_name = second_name;
        this.user_role = user_role;
    }
}

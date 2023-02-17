package ru.tinkoff.lab.tripAPI.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String id;

    private String email;

    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("second_name")
    private String secondName;

    @JsonProperty("user_role")
    private String userRole;

    private List<User> subordinates;

    private String salt;

    public User(String email, String password, String firstName, String secondName, String userRole) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userRole = userRole;
    }

    public User(String email, String password, String firstName, String secondName, String userRole, String salt) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userRole = userRole;
        this.salt = salt;
    }

    public User(UUID id, String email, String password, String firstName, String secondName, String userRole) {
        this.id = id.toString();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userRole = userRole;
    }
}

package ru.tinkoff.lab.tripAPI.business;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User {
    private String id;

    private String email;

    @Length(min = 6)
    private String password;

    private String firstName;

    private String secondName;

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

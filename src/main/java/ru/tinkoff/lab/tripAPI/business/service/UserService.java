package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.exceptions.UserCreateException;
import ru.tinkoff.lab.tripAPI.exceptions.UserNotFoundException;
import ru.tinkoff.lab.tripAPI.mapping.UserMapper;
import ru.tinkoff.lab.tripAPI.mapping.UserRelationMapper;
import ru.tinkoff.lab.tripAPI.security.utils.PasswordEncoder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    private final UserRelationMapper userRelationMapper;

    private final PasswordEncoder passwordEncoder;

    private final PasswordEncoder passwordEncoder;

    public Id createUser(User user) {
        try {
            String salt = passwordEncoder.generateSalt();
            String password = passwordEncoder.generatePassword(user.getPassword(), salt);
            user.setSalt(salt);
            user.setPassword(password);
            return userMapper.insertUser(user);
        } catch (UserCreateException e) {
            throw new UserCreateException("Error with creating this user");
        }
    }
    //TODO: issue: mybatis throws forbidden if something wrong with request(ex. if user already exists in db)

    public User findById(UUID uuid) {
        User user = userMapper.selectUser(uuid);
        if (user == null) {
            throw new UserNotFoundException("User with id = " + uuid + " was not found");
        }
        return user;
    }

    public User findByEmail(String email) {
        User user = userMapper.selectUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email = " + email + " was not found");
        }
        return user;
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(UUID id) {
        userMapper.deleteUser(id);
    }

    public void createRelation(UUID approverId, UUID userId) {
        userRelationMapper.insertUserRelation(approverId, userId);
    }

    public void deleteRelation(UUID approverId, UUID userId) {
        userRelationMapper.deleteUserRelation(approverId, userId);
    }
}
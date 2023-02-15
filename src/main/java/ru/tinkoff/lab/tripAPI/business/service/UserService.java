package ru.tinkoff.lab.tripAPI.business.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.mapping.UserMapper;

import java.util.UUID;

@Service
public class UserService {
    private final UserMapper userMapper;
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Id createUser(User user) {
        return userMapper.insertUser(user);
    }

    public User getUser(String userId) {
        return userMapper.selectUser(UUID.fromString(userId));
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}

package ru.tinkoff.lab.tripAPI.business.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.mapping.UserMapper;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Id createUser(User user) {
        return userMapper.insertUser(user);
    }

    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    public User getUser(String userId) {
        return userMapper.selectUser(userId);
    }
}

package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.mapping.UserMapper;
import ru.tinkoff.lab.tripAPI.mapping.UserRelationMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    private final UserRelationMapper userRelationMapper;

    public Id createUser(User user) {
        try {
            return userMapper.insertUser(user);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  e.getMessage());
        }
    }

    public User findById(UUID uuid) {
        User user = userMapper.selectUser(uuid);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id = " + uuid + " was not found");
        }
        return user;
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(UUID id) {
        userMapper.deleteUser(id);
    }

    public void createRelation(UUID bossId, UUID userId) {
        userRelationMapper.insertUserRelation(bossId, userId);
    }

    public void deleteRelation(UUID bossId, UUID userId) {
        userRelationMapper.deleteUserRelation(bossId, userId);
    }
}
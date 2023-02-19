package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
            return null;
        }
    }

    public User findById(String userId) {
        return userMapper.selectUser(UUID.fromString(userId));
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(String id) {
        userMapper.deleteUser(UUID.fromString(id));
    }

    public void createRelation(String bossId, String userId) {
        userRelationMapper.insertUserRelation(UUID.fromString(bossId), UUID.fromString(userId));
    }

    public void deleteRelation(String bossId, String userId) {
        userRelationMapper.deleteUserRelation(UUID.fromString(bossId), UUID.fromString(userId));
    }
}
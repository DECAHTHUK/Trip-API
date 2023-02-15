package ru.tinkoff.lab.tripAPI.business.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.mapping.UserRelationMapper;

import java.util.UUID;

@Service
public class UserRelationService {
    private final UserRelationMapper mapper;

    public UserRelationService(UserRelationMapper mapper) {this.mapper = mapper;}

    public void createRelation(String boss, String user) {
        mapper.insertUserRelation(UUID.fromString(boss), UUID.fromString(user));
    }

    public void deleteRelation(String boss, String user) {
        mapper.deleteUserRelation(UUID.fromString(boss), UUID.fromString(user));
    }
}

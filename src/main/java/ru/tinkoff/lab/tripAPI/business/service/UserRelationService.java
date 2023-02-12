package ru.tinkoff.lab.tripAPI.business.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.mapping.UserRelationMapper;

@Service
public class UserRelationService {
    private final UserRelationMapper mapper;

    public UserRelationService(UserRelationMapper mapper) {this.mapper = mapper;}

    public void createRelation(String boss, String user) {
        mapper.insertUserRelation(boss, user);
    }

    public void deleteRelation(String boss, String user) {
        mapper.deleteUserRelation(boss, user);
    }
}

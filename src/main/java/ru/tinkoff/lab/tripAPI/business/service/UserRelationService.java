package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.mapping.UserRelationMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRelationService {
    private final UserRelationMapper mapper;

    public void createRelation(String boss, String user) {
        mapper.insertUserRelation(UUID.fromString(boss), UUID.fromString(user));
    }

    public void deleteRelation(String boss, String user) {
        mapper.deleteUserRelation(UUID.fromString(boss), UUID.fromString(user));
    }
}

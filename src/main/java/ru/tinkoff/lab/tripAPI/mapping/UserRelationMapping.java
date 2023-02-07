package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRelationMapping {
    // CRUD for user_relations table (update and get are pretty much useless)

    @Insert("INSERT INTO users_relarions (boss_id, user_id) " +
            "VALUES (#{bossId}, #{userId});")
    void insertUserRelation(String bossId, String userId);

    @Delete("DELETE FROM users_relations " +
            "WHERE boss_id = uuid(#{bossId}) and user_id = uuid(#{userId});")
    void deleteUserRelation(String bossId, String userId);
}

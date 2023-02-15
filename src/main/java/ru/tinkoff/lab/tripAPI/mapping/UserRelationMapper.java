package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

/**
 * CRUD for user_relations table (update and get are pretty much useless)
 */
@Mapper
public interface UserRelationMapper {

    @Insert("""
            INSERT INTO users_relarions (boss_id, user_id)
            VALUES ('${bossId}', '${userId}');
            """)
    void insertUserRelation(@Param("bossId") UUID bossId, @Param("userId") UUID userId);

    @Delete("""
            DELETE FROM users_relations
            WHERE boss_id = '${bossId}' and user_id = '${userId}');
            """)
    void deleteUserRelation(@Param("bossId") UUID bossId, @Param("userId") UUID userId);
}

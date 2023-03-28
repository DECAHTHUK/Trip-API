package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for user_relations table (update and get are pretty much useless)
 */
@Mapper
public interface UserRelationMapper {

    @Insert("""
            INSERT INTO users_relations (approver_id, user_id)
            VALUES ('${approverId}', '${userId}');
            """)
    void insertUserRelation(@Param("approverId") UUID approverId, @Param("userId") UUID userId);

    @Delete("""
            DELETE FROM users_relations
            WHERE approver_id = '${approverId}' and user_id = '${userId}';
            """)
    void deleteUserRelation(@Param("approverId") UUID approverId, @Param("userId") UUID userId);
    
    @Select("""
            SELECT approver_id FROM users_relations WHERE user_id='${userId}';
            """)
    List<String> selectApproversIds(@Param("userId") UUID userId);
}

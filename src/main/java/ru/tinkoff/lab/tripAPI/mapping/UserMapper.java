package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;

import java.util.List;
import java.util.UUID;

/**
 * User CRUD
 */
@Mapper
public interface UserMapper {

    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO users (email, password, first_name, second_name, user_role)
            VALUES(#{email}, #{password}, #{firstName}, #{secondName}, #{userRole})
            RETURNING id;
            """)
    Id insertUser(User user) throws RuntimeException;

    @Results(value = {
            @Result(property = "subordinates", javaType = List.class,
                    column = "id", many = @Many(select = "selectSubordinates")),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "secondName", column = "second_name"),
            @Result(property = "userRole", column = "user_role"),
            @Result(column = "id", property = "id")
    })
    @Select("""
            SELECT u.id, u.email, u.password, u.first_name, u.second_name, u.user_role
            FROM users as u
            WHERE u.id = '${uuid}';
            """)
    User selectUser(@Param("uuid") UUID uuid);

    @Results(value = {
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "secondName", column = "second_name"),
            @Result(property = "userRole", column = "user_role")
    })
    @Select("""
            SELECT u.id, u.email, u.password, u.first_name, u.second_name, u.user_role
            FROM users as u
            WHERE u.email = #{email};
            """)
    User selectUserByEmail(String email);

    /**
     * Additional query for our many-to-many relationship(myBatis works only this way)
     */
    @Results(value = {
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "secondName", column = "second_name"),
            @Result(property = "userRole", column = "user_role")
    })
    @Select("""
            SELECT u.id, u.email, u.password, u.first_name, u.second_name, u.user_role
            FROM users_relations as r
            JOIN users as u on u.id = r.user_id
            WHERE r.approver_id = '${uuid}';
            """)
    User selectSubordinates(@Param("uuid") UUID uuid);

    @Update("""
            UPDATE users
            SET email=#{email}, password=#{password}, first_name=#{firstName},
            second_name=#{secondName}, user_role=#{userRole}
            WHERE id = uuid(#{id});
            """)
    void updateUser(User user);

    @Delete("""
            DELETE FROM users
            WHERE id = '${userId}';
            """)
    void deleteUser(@Param("userId") UUID userId);
}

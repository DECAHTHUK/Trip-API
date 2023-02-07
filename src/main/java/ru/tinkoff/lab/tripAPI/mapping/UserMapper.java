package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;

import java.util.List;

@Mapper
public interface UserMapper {

    // User CRUD

    @Select("SELECT * FROM users;")
    List<User> selectAllUsers();

    @Result(property = "id", column = "id")
    @Select("INSERT INTO users (email, password, first_name, second_name, user_role)" +
            "VALUES(#{email}, #{password}, #{first_name}, #{second_name}, #{user_role})" +
            "RETURNING id;")
    Id insertUser(User user);

    @Result(property = "subordinates", javaType = List.class,
            column = "id", many = @Many(select = "selectSubordinates"))
    @Select("SELECT u.id, u.email, u.password, u.first_name, u.second_name, u.user_role " +
            "FROM users as u " +
            "WHERE u.id = uuid(#{userId});")
    User selectUser(String userId);

    // Additional query for our many-to-many relationship(myBatis works only this way)
    @Select("SELECT u.id, u.email, u.password, u.first_name, u.second_name, u.user_role " +
            "FROM users_relations as r " +
            "JOIN users as u on u.id = r.user_id " +
            "WHERE r.boss_id = uuid(#{userId});")
    User selectSubordinates(String userId);

    @Update("UPDATE users " +
            "SET email=#{email}, password=#{password}, first_name=#{first_name}, " +
            "second_name=#{second_name}, user_role=#{user_role} " +
            "WHERE id = uuid(#{id});")
    void updateUser(User user);

    @Delete("DELETE FROM users " +
            "WHERE id = uuid(#{userId});")
    void deleteUser(String userId);
}

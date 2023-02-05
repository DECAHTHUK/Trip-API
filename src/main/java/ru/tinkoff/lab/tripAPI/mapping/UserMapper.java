package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users;")
    List<User> selectAllUsers();

    @Result(property = "id", column = "id")
    @Select("INSERT INTO users (email, password, first_name, second_name, user_role)" +
            "values(#{email}, #{password}, #{first_name}, #{second_name}, #{user_role})" +
            "RETURNING id;")
    Id insertUser(User user);

    @Select("SELECT * " +
            "FROM users " +
            "WHERE id = uuid(#{userId});")
    User selectUser(String userId);
}

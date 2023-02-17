package ru.tinkoff.lab.tripAPI.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;

@RunWith(SpringRunner.class)
@MybatisTest
@Import(UserService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTest {

    @Autowired
    UserService userService;

    @Test
    public void createNewUserTest() {
        User user = new User("email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER",
                "salt");
        Id uuid = userService.createUser(user);
        Assertions.assertNotNull(uuid);
        System.out.println(uuid.getId());
    }
}

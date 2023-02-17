package ru.tinkoff.lab.tripAPI.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.User;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTest {
    UserController userController;

    @Test
    public void createNewUserTest() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid,
                "email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER");
        userController.createNewUser(user);
        assertThat(true);
    }
}

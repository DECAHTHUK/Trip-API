package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.exceptions.UserCreateException;
import ru.tinkoff.lab.tripAPI.exceptions.UserNotFoundException;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.utils.JwtUtils;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({UserService.class, JwtUtils.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class UserServiceTest {

    @Autowired
    UserService userService;

    User user;

    Id bossId;
    Id subordinateId;

    @BeforeAll
    public void init() {
        user = new User("email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER");
        Id userId = userService.createUser(user);
        user.setId(userId.getId());

        bossId = userService.createUser(new User("krutoi1337@gmail.com",
                "qwertyuio", "Rusya", "Talanov", "ADMIN"));
        subordinateId = userService.createUser(new User("slavakpss@gmail.com",
                "12345678", "Lyosha", "Sultanov", "USER"));
        userService.createRelation(UUID.fromString(bossId.getId()), UUID.fromString(subordinateId.getId()));
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created user gets returned from db")
    public void testFindById_shouldReturnCorrespondingUser() {
        User userFromDB = userService.findById(UUID.fromString(user.getId()));

        assertEquals(user.getEmail(), userFromDB.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Test if user in the db is getting updated")
    public void testUpdateUser_shouldChangeValuesInDatabase() {
        User updatedUser = userService.findById(UUID.fromString(user.getId()));
        updatedUser.setEmail("newEmail@mail.ru");
        userService.updateUser(updatedUser);

        User newUser = userService.findById(UUID.fromString(user.getId()));
        assertEquals("newEmail@mail.ru", newUser.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Test user getting deleted")
    public void testDeleteUser_shouldDeleteFromDatabase() {
        assertNotNull(userService.findById(UUID.fromString(user.getId())));

        userService.deleteUser(UUID.fromString(user.getId()));

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.findById(UUID.fromString(user.getId())));

        assertEquals("User with id = " + user.getId() + " was not found", thrown.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Test the email unique constraint")
    public void testCreateUser_whenEmailAlreadyInDatabase_shouldReturnNull() {
        UserCreateException thrown = assertThrows(UserCreateException.class, () -> userService.createUser(user));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Test if user's subordinates are accessed properly")
    public void testFindById_shouldReturnSubordinates() {
        User boss = userService.findById(UUID.fromString(bossId.getId()));

        assertNotNull(boss.getSubordinates());

        User subordinate = userService.findById(UUID.fromString(subordinateId.getId()));

        assertNotEquals(0,
                boss.getSubordinates()
                        .stream()
                        .filter(t -> t.getEmail().equals(subordinate.getEmail()))
                        .count());
    }

    @Test
    @Order(6)
    @DisplayName("Test new subordinates should be returned")
    public void testCreateDeleteRelation_shouldChangeOutput() {
        //Arrange
        User boss = userService.findById(UUID.fromString(bossId.getId()));
        int initialSize = boss.getSubordinates().size();
        Id id = userService.createUser(new User("sobaka@sobaka.com",
                "1223", "John", "Doe", "USER"));

        //Act
        //Addition check
        userService.createRelation(UUID.fromString(boss.getId()), UUID.fromString(id.getId()));
        boss = userService.findById(UUID.fromString(boss.getId()));
        int sizeAfterAddition = boss.getSubordinates().size();

        assertEquals(1, sizeAfterAddition - initialSize);

        //Deletion check
        userService.deleteRelation(UUID.fromString(boss.getId()), UUID.fromString(subordinateId.getId()));
        boss = userService.findById(UUID.fromString(boss.getId()));
        int sizeAfterDeletion = boss.getSubordinates().size();

        assertEquals(initialSize, sizeAfterDeletion);
        assertTrue(boss.getSubordinates().stream().anyMatch(t -> t.getId().equals(id.getId())));
    }

}

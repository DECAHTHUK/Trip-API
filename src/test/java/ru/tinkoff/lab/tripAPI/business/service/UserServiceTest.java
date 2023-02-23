package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;


import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({UserService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Autowired
    UserService userService;

    User user = new User("email@mail.ru",
            "12345678",
            "John",
            "Smith",
            "USER",
            "salt");

    Id bossId = new Id();
    Id subordinateId = new Id();

    @BeforeAll
    public void init() {
        Id userId = userService.createUser(user);
        user.setId(userId.getId());

        bossId = userService.createUser(new User("krutoi1337@gmail.com",
                "qwertyuio", "Rusya", "Talanov", "user"));
        subordinateId = userService.createUser(new User("slavakpss@gmail.com",
                "12345678", "Lyosha", "Sultanov", "user"));
        userService.createRelation(bossId.getId(), subordinateId.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created user gets returned from db")
    public void testFindById_shouldReturnCorrespondingUser() {
        User userFromDB = userService.findById(user.getId());

        assertEquals(user.getEmail(), userFromDB.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Test if user in the db is getting updated")
    public void testUpdateUser_shouldChangeValuesInDatabase() {
        User updatedUser = userService.findById(user.getId());
        updatedUser.setEmail("newEmail@mail.ru");
        userService.updateUser(updatedUser);

        User newUser = userService.findById(user.getId());
        assertEquals("newEmail@mail.ru", newUser.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Test user getting deleted")
    public void testDeleteUser_shouldDeleteFromDatabase() {
        assertNotNull(userService.findById(user.getId()));

        userService.deleteUser(user.getId());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> userService.findById(user.getId()));

        assertEquals(HttpStatus.NOT_FOUND + " \"User with id = " + user.getId() + " was not found\"", thrown.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Test the email unique constraint")
    public void testCreateUser_whenEmailAlreadyInDatabase_shouldReturnNull() {
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> userService.createUser(user));
        assertEquals(HttpStatus.BAD_REQUEST + " \"Bad request for User entity\"", thrown.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Test if user's subordinates are accessed properly")
    public void testFindById_shouldReturnSubordinates() {
        User boss = userService.findById(bossId.getId());

        assertNotNull(boss.getSubordinates());

        User subordinate = userService.findById(subordinateId.getId());

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
        User boss = userService.findById(bossId.getId());
        int initialSize = boss.getSubordinates().size();
        Id id = userService.createUser(new User("sobaka@sobaka.com",
                "1223", "John", "Doe", "USER"));

        //Act
        //Addition check
        userService.createRelation(boss.getId(), id.getId());
        boss = userService.findById(boss.getId());
        int sizeAfterAddition = boss.getSubordinates().size();

        assertEquals(1, sizeAfterAddition - initialSize);

        //Deletion check
        userService.deleteRelation(boss.getId(), subordinateId.getId());
        boss = userService.findById(boss.getId());
        int sizeAfterDeletion = boss.getSubordinates().size();

        assertEquals(initialSize, sizeAfterDeletion);
        assertTrue(boss.getSubordinates().stream().anyMatch(t -> t.getId().equals(id.getId())));
    }

}

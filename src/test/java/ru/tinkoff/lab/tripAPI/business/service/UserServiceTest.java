package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
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

    @BeforeAll
    public void createNewUser() {
        Id userId = userService.createUser(user);
        user.setId(userId.getId());
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

        assertNull(userService.findById(user.getId()));
    }

    @Test
    @Order(4)
    @DisplayName("Test the email unique constraint")
    public void testCreateUser_whenEmailAlreadyInDatabase_shouldReturnNull() {
        userService.createUser(user);
        assertNull(userService.createUser(user));
    }

    @Test
    @Order(5)
    @DisplayName("Test if user's subordinates are accessed properly")
    public void testFindById_shouldReturnSubordinates() {
        User boss = userService.findById("15322743-91db-44fc-b121-cdee08ac71cb");

        assertNotNull(boss.getSubordinates());

        User subordinate = userService.findById("c1a0a7cc-6820-4b21-aab7-ddccce572faf");

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
        User boss = userService.findById("15322743-91db-44fc-b121-cdee08ac71cb");
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
        userService.deleteRelation(boss.getId(), "c1a0a7cc-6820-4b21-aab7-ddccce572faf");
        boss = userService.findById(boss.getId());
        int sizeAfterDeletion = boss.getSubordinates().size();

        assertEquals(initialSize, sizeAfterDeletion);
        assertTrue(boss.getSubordinates().stream().anyMatch(t -> t.getId().equals(id.getId())));
    }

}

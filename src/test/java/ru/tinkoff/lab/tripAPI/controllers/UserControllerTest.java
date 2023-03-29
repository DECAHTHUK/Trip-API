package ru.tinkoff.lab.tripAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.AccommodationDestinationTripService;
import ru.tinkoff.lab.tripAPI.business.service.NotificationService;
import ru.tinkoff.lab.tripAPI.business.service.RequestService;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.exceptions.UserNotFoundException;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.AuthService;
import ru.tinkoff.lab.tripAPI.security.LoginController;
import ru.tinkoff.lab.tripAPI.security.filtering.JwtFilter;
import ru.tinkoff.lab.tripAPI.security.filtering.SecurityConfig;
import ru.tinkoff.lab.tripAPI.security.models.LoginRequest;
import ru.tinkoff.lab.tripAPI.security.utils.JwtProvider;
import ru.tinkoff.lab.tripAPI.security.utils.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = {UserController.class, LoginController.class})
@Import({UserService.class, UuidTypeHandler.class, NotificationService.class, RequestService.class, AccommodationDestinationTripService.class,
        PasswordEncoder.class, JwtProvider.class, JwtFilter.class, SecurityConfig.class, AuthService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class UserControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    User user = new User(
            "rs_xdm@inst.com",
            "qwertyuiop",
            "Ruslan",
            "Sultanov",
            "ADMIN"
    );

    User subUser = new User(
            "zamay@mail.ru",
            "zamaykrasava",
            "Andrey",
            "Zamay",
            "USER"
    );

    String userJwt;
    String adminJwt;

    @Test
    @Order(1)
    @DisplayName("Test user gets created and returned")
    void testCreateGetUser() throws Exception {
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(user));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        user.setId(id.getId());
        user.setSubordinates(List.of());

        //getting jwt token
        requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(user.getEmail(), user.getPassword())));

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();
        System.out.println("RBP: "  + responseBodyPost);
        adminJwt = responseBodyPost;
        System.out.println(adminJwt);

        //Testing get request for newly created user
        RequestBuilder requestBuilderGet =
                MockMvcRequestBuilders.get("/users/" + user.getId())
                        .header("Authorization", "Bearer " + adminJwt)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        User userFromRequest = mapper.readValue(responseBodyGet, User.class);
        assertEquals(user.getEmail(), userFromRequest.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Test user relations gets created")
    public void testCreateUserRelation() throws Exception {
        // Adding second user to create relations
        RequestBuilder requestBuilderPostUser = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(subUser));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPostUser).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        subUser.setId(id.getId());
        subUser.setSubordinates(List.of());

        //Testing get request for newly created user
        RequestBuilder requestBuilderGetSubUser =
                MockMvcRequestBuilders.get("/users/" + subUser.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGetSubUser = mockMvc.perform(requestBuilderGetSubUser).andReturn();
        String responseBodyGetSubUser = mvcResultGetSubUser.getResponse().getContentAsString();

        User userFromRequest = mapper.readValue(responseBodyGetSubUser, User.class);
        assertEquals(subUser, userFromRequest);
        assertEquals(0, user.getSubordinates().size());

        // Testing post new relations
        RequestBuilder requestBuilderPostRelations = MockMvcRequestBuilders
                .post("/users/" + user.getId() + "/subordinates/" + subUser.getId());

        mockMvc.perform(requestBuilderPostRelations);

        RequestBuilder requestBuilderGetUser =
                MockMvcRequestBuilders.get("/users/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGetUser = mockMvc.perform(requestBuilderGetUser).andReturn();
        String responseBodyGetUser = mvcResultGetUser.getResponse().getContentAsString();

        //Testing if relations were created successfully
        User bossUser = mapper.readValue(responseBodyGetUser, User.class);
        assertNotNull(bossUser.getSubordinates());
        assertNotEquals(0,
                bossUser.getSubordinates()
                        .stream()
                        .filter(t -> t.getEmail().equals(subUser.getEmail()))
                        .count());
    }

    @Test
    @Order(3)
    @DisplayName("Test user relations gets deleted")
    public void testDeleteUserRelation() throws Exception {
        //Testing relations get deleted
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders
                .delete("/users/" + user.getId() + "/subordinates/" + subUser.getId());

        mockMvc.perform(requestBuilderDelete);

        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGetUser = mvcResultGet.getResponse().getContentAsString();

        //Taking result of deleting
        User userFromRequest = mapper.readValue(responseBodyGetUser, User.class);
        assertNotNull(userFromRequest);
        assertEquals(0, userFromRequest.getSubordinates().size());
    }

    @Test
    @Order(4)
    @DisplayName("Test user gets updated and deleted")
    public void testUpdateDeleteUser() throws Exception {
        //Updating user
        user.setFirstName("Lyosha");
        user.setSecondName("Talanov");
        user.setUserRole("admin");
        user.setPassword("lalalala");

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(user));

        mockMvc.perform(requestBuilderPut);

        //Getting user to check if it was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders.get("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        User userFromRequest = mapper.readValue(responseBodyGet, User.class);
        assertEquals("Lyosha", userFromRequest.getFirstName());
        assertEquals("Talanov", userFromRequest.getSecondName());
        assertEquals("admin", userFromRequest.getUserRole());
        assertEquals("lalalala", userFromRequest.getPassword());

        //Testing user to check if it was deleted
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders.delete("/users/" + user.getId());
        mockMvc.perform(requestBuilderDelete);

        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        UserNotFoundException exception = (UserNotFoundException) mvcResultGet.getResolvedException();

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}

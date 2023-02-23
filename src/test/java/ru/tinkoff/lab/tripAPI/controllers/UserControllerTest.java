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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = UserController.class)
@Import({UserService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    User user = new User(
            "rs_xdm@inst.com",
            "qwertyuiop",
            "Ruslan",
            "Sultanov",
            "user"
    );

    User subUser = new User(
            "zamay@mail.ru",
            "zamaykrasava",
            "Andrey",
            "Zamay",
            "user"
    );

    @Test
    @Order(1)
    @DisplayName("Test user gets created and returned")
    void testCreateGetUser() throws Exception {
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = new ObjectMapper().readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        user.setId(id.getId());
        user.setSubordinates(List.of());

        //Testing get request for newly created user
        RequestBuilder requestBuilderGet =
                MockMvcRequestBuilders.get("/users/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        User userFromRequest = new ObjectMapper().readValue(responseBodyGet, User.class);
        assertEquals(user, userFromRequest);
    }

    @Test
    @Order(2)
    @DisplayName("Test user relations gets created")
    public void testCreateUserRelation() throws Exception {
        RequestBuilder requestBuilderPostUser = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(subUser));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPostUser).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = new ObjectMapper().readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        subUser.setId(id.getId());
        subUser.setSubordinates(List.of());

        //Testing get request for newly created user
        RequestBuilder requestBuilderGetSubUser =
                MockMvcRequestBuilders.get("/users/" + subUser.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGetSubUser = mockMvc.perform(requestBuilderGetSubUser).andReturn();
        String responseBodyGetSubUser = mvcResultGetSubUser.getResponse().getContentAsString();

        User userFromRequest = new ObjectMapper().readValue(responseBodyGetSubUser, User.class);
        assertEquals(subUser, userFromRequest);
        assertEquals(0, user.getSubordinates().size());

        // Testing post new relations
        RequestBuilder requestBuilderPostRelations = MockMvcRequestBuilders
                .post("/users/" + user.getId() + "/subordinate/" + subUser.getId());

        mockMvc.perform(requestBuilderPostRelations);

        RequestBuilder requestBuilderGetUser =
                MockMvcRequestBuilders.get("/users/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGetUser = mockMvc.perform(requestBuilderGetUser).andReturn();
        String responseBodyGetUser = mvcResultGetUser.getResponse().getContentAsString();

        User bossUser = new ObjectMapper().readValue(responseBodyGetUser, User.class);
        assertEquals(user, bossUser);
        assertEquals(1, bossUser.getSubordinates().size());
    }

    @Test
    @Order(3)
    @DisplayName("Test user gets updated and deleted")
    public void testUpdateDeleteUser() throws Exception {
        //Updating user
        user.setFirstName("Lyosha");
        user.setSecondName("Talanov");
        user.setUserRole("admin");
        user.setPassword("lalalala");

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user));

        mockMvc.perform(requestBuilderPut);

        //Getting user to check if it was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders.get("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        User userFromRequest = new ObjectMapper().readValue(responseBodyGet, User.class);
        assertEquals("Lyosha", userFromRequest.getFirstName());
        assertEquals("Talanov", userFromRequest.getSecondName());
        assertEquals("admin", userFromRequest.getUserRole());
        assertEquals("lalalala", userFromRequest.getPassword());

        //Testing user to check if it was deleted
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders.delete("/users/" + user.getId());
        mockMvc.perform(requestBuilderDelete);

        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        ResponseStatusException exception = (ResponseStatusException) mvcResultGet.getResolvedException();

        assert exception != null;
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}

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
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.*;
import ru.tinkoff.lab.tripAPI.exceptions.OfficeNotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = {OfficeController.class, LoginController.class, UserController.class})
@Import({UserService.class, UuidTypeHandler.class, NotificationService.class, RequestService.class, AccommodationDestinationTripService.class,
        PasswordEncoder.class, JwtProvider.class, JwtFilter.class, SecurityConfig.class, AuthService.class, OfficeService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class OfficeControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    Office office = new Office("Nizhny Novgorod", "Andrey is there!");

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

    @BeforeAll
    public void init() throws Exception {
        // Adding user
        userService.createUser(user);

        // getting jwt token
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(user.getEmail(), user.getPassword())));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();

        adminJwt = mvcResultPost.getResponse().getContentAsString();

        requestBuilderPost = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(subUser));

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        subUser.setId(id.getId());
        subUser.setSubordinates(List.of());

        //getting user's jwt token
        requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(subUser.getEmail(), subUser.getPassword())));

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();
        userJwt = responseBodyPost;
    }

    @Test
    @Order(1)
    @DisplayName("Test office gets created and returned")
    void testCreateGetOffice() throws Exception {

        // Creating office
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders.post("/offices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(office))
                .header("Authorization", "Bearer " + adminJwt);

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        office.setId(id.getId());

        // Testing get request for newly created office
        RequestBuilder requestBuilderGet =
                MockMvcRequestBuilders.get("/offices/" + id.getId())
                        .header("Authorization", "Bearer " + adminJwt)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Office officeFromRequest = mapper.readValue(responseBodyGet, Office.class);
        assertEquals(office, officeFromRequest);
    }

    @Test
    @Order(2)
    @DisplayName("Test user's access")
    public void testUsersAccess() throws Exception {
        RequestBuilder requestBuilderGet =
                MockMvcRequestBuilders.get("/offices/" + office.getId())
                        .header("Authorization", "Bearer " + userJwt)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilderGet).andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    @DisplayName("Test office gets updated and deleted")
    public void testUpdateDeleteOffice() throws Exception {
        // Updating office
        office.setAddress("updated address");
        office.setDescription("updated description");
        RequestBuilder requestBuilderPut = MockMvcRequestBuilders.put("/offices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + adminJwt)
                .content(mapper.writeValueAsString(office));

        mockMvc.perform(requestBuilderPut);

        //Getting office to check for updates
        RequestBuilder requestBuilderGet =
                MockMvcRequestBuilders.get("/offices/" + office.getId())
                        .header("Authorization", "Bearer " + adminJwt)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Office officeFromRequest = mapper.readValue(responseBodyGet, Office.class);
        assertEquals("updated address", officeFromRequest.getAddress());
        assertEquals("updated description", officeFromRequest.getDescription());

        //Deleting office
        RequestBuilder requestBuilderDelete =
                MockMvcRequestBuilders.delete("/offices/" + office.getId())
                        .header("Authorization", "Bearer " + adminJwt);

        mockMvc.perform(requestBuilderDelete);

        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        OfficeNotFoundException exception = (OfficeNotFoundException) mvcResultGet.getResolvedException();

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}

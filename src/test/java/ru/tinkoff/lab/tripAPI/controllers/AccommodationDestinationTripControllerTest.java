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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.business.service.*;
import ru.tinkoff.lab.tripAPI.exceptions.AccommodationNotFoundException;
import ru.tinkoff.lab.tripAPI.exceptions.DestinationNotFoundException;
import ru.tinkoff.lab.tripAPI.exceptions.TripNotFoundException;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.AuthService;
import ru.tinkoff.lab.tripAPI.security.LoginController;
import ru.tinkoff.lab.tripAPI.security.filtering.JwtFilter;
import ru.tinkoff.lab.tripAPI.security.filtering.SecurityConfig;
import ru.tinkoff.lab.tripAPI.security.models.LoginRequest;
import ru.tinkoff.lab.tripAPI.security.utils.JwtProvider;
import ru.tinkoff.lab.tripAPI.security.utils.PasswordEncoder;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = {AccommodationDestinationTripController.class, LoginController.class, UserController.class})
@Import({UserService.class, UuidTypeHandler.class, NotificationService.class, RequestService.class, AccommodationDestinationTripService.class,
        PasswordEncoder.class, JwtProvider.class, JwtFilter.class, SecurityConfig.class, AuthService.class, OfficeService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application.yaml")
@DirtiesContext
public class AccommodationDestinationTripControllerTest {


    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    @Autowired
    RequestService requestService;

    @Autowired
    OfficeService officeService;

    @Autowired
    private MockMvc mockMvc;

    Accommodation accommodation = new Accommodation(
            "Republic street 28",
            "booking.com/DEJDNkdsmdneuwij12893hd"
    );

    DestinationDto destinationDto = new DestinationDto("Zarechnaya 7", "9");
    Office office = new Office("Avenue 10", "Cool office");

    RequestDto requestDto = new RequestDto("Approved request",
            "Nothing",
            new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2020 - 1901, 12, 15, 12, 0, 0, 0),
            "https:/somesite.com/JAOwe7IW78daAw1idh");

    User worker = new User("email@mail.ru",
            "12345678",
            "John",
            "Smith",
            "USER",
            "something");
    User user = new User(
            "rs_xdm@inst.com",
            "qwertyuiop",
            "Ruslan",
            "Sultanov",
            "ADMIN"
    );

    String workerJwt;
    String adminJwt;

    TripDto tripDto = new TripDto();

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
                .content(mapper.writeValueAsString(worker))
                .header("Authorization", "Bearer " + adminJwt);

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id workerId = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(workerId);
        worker.setId(workerId.getId());

        //getting worker's jwt token
        requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(worker.getEmail(), worker.getPassword())));

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();
        workerJwt = responseBodyPost;
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created accommodation gets returned")
    public void testCreateGetAccommodation() throws Exception {
        // Testing post accommodation method
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders
                .post("/accommodations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(accommodation));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        accommodation.setId(id.getId());

        //Testing get accommodation method
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/accommodations/" + accommodation.getId())
                .header("Authorization", "Bearer " + workerJwt)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Accommodation accommodationFromRequest = new ObjectMapper().readValue(responseBodyGet, Accommodation.class);
        assertEquals(accommodation, accommodationFromRequest);
    }

    @Test
    @Order(2)
    @DisplayName("Test if newly created destination gets returned")
    public void testCreateGetDestination() throws Exception {
        Id officeId = officeService.createOffice(office);
        assertNotNull(officeId);
        office.setId(officeId.getId());
        destinationDto.setOfficeId(office.getId());

        //Testing post destination method
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders
                .post("/destinations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(destinationDto));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        destinationDto.setId(id.getId());

        //Testing newly created destination gets returned
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/destinations/" + destinationDto.getId())
                .header("Authorization", "Bearer " + workerJwt)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Destination destination = mapper.readValue(responseBodyGet, Destination.class);
        assertNotNull(destination);
    }

    @Test
    @Order(3)
    @DisplayName("Test if newly created trip gets returned")
    public void testCreateGetTrip() throws Exception {
        requestDto.setDestinationId(destinationDto.getId());
        requestDto.setWorkerId(worker.getId());

        Id requestId = requestService.createRequest(requestDto);
        requestDto.setId(requestId.getId());

        tripDto.setTripStatus(TripStatus.PENDING);
        tripDto.setAccommodationId(accommodation.getId());
        tripDto.setDestinationId(destinationDto.getId());
        tripDto.setRequestId(requestDto.getId());

        //Testing post trip method
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders
                .post("/trips")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(tripDto));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(id);
        tripDto.setId(id.getId());

        //Testing get trip method
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/trips/" + tripDto.getId())
                .header("Authorization", "Bearer " + workerJwt)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Trip trip = mapper.readValue(responseBodyGet, Trip.class);
        assertNotNull(trip);
    }

    @Test
    @Order(4)
    @DisplayName("Testing unauthorized user")
    public void testUnauthorizedUser() throws Exception {
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/trips/" + tripDto.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilderGet).andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @DisplayName("Test if trip gets updated and deleted")
    public void testUpdateDeleteTrip() throws Exception {
        tripDto.setTripStatus(TripStatus.COMPLETED);

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/trips")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(tripDto));

        mockMvc.perform(requestBuilderPut);

        //Getting trip to check if it was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/trips/" + tripDto.getId())
                .header("Authorization", "Bearer " + workerJwt)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Trip tripFromRequest = mapper.readValue(responseBodyGet, Trip.class);
        assertNotNull(tripFromRequest);
        assertEquals(tripDto.getTripStatus(), tripFromRequest.getTripStatus());

        //Testing delete method
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders
                .delete("/trips/" + tripDto.getId())
                .header("Authorization", "Bearer " + workerJwt);

        mockMvc.perform(requestBuilderDelete);

        //Trying to get the trip to check if it was deleted
        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        TripNotFoundException exception = (TripNotFoundException) mvcResultGet.getResolvedException();

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Test if destination gets updated and deleted")
    public void testUpdateDeleteDestination() throws Exception {
        destinationDto.setDescription("New description");
        destinationDto.setSeatPlace("New seat place");

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/destinations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(destinationDto));

        mockMvc.perform(requestBuilderPut);

        //Getting trip to check if it was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/destinations/" + destinationDto.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Destination destinationFromRequest = new ObjectMapper().readValue(responseBodyGet, Destination.class);
        assertNotNull(destinationFromRequest);
        assertEquals(destinationDto.getDescription(), destinationFromRequest.getDescription());
        assertEquals(destinationDto.getSeatPlace(), destinationFromRequest.getSeatPlace());

        requestService.deleteRequest(UUID.fromString(requestDto.getId()));
        //Testing delete method
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders
                .delete("/destinations/" + destinationDto.getId())
                .header("Authorization", "Bearer " + workerJwt);

        mockMvc.perform(requestBuilderDelete);

        //Trying to get the trip to check if it was deleted
        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        DestinationNotFoundException exception = (DestinationNotFoundException) mvcResultGet.getResolvedException();

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("Test if accommodation gets updated and deleted")
    public void testUpdateDeleteAccommodation() throws Exception {
        accommodation.setAddress("New address");
        accommodation.setBookingUrl("New url");
        //assertNotNull(accommodation);

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/accommodations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(accommodation));

        mockMvc.perform(requestBuilderPut);

        //Getting accommodation to check if it was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/accommodations/" + accommodation.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Accommodation accommodationFromRequest = new ObjectMapper().readValue(responseBodyGet, Accommodation.class);
        assertEquals(accommodation, accommodationFromRequest);

        //Testing delete method
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders
                .delete("/accommodations/" + accommodation.getId())
                .header("Authorization", "Bearer " + workerJwt);

        mockMvc.perform(requestBuilderDelete);

        //Trying to get the trip to check if it was deleted
        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        AccommodationNotFoundException exception = (AccommodationNotFoundException) mvcResultGet.getResolvedException();

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
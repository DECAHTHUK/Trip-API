package ru.tinkoff.lab.tripAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestStatusChangeDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.business.service.*;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.AuthService;
import ru.tinkoff.lab.tripAPI.security.LoginController;
import ru.tinkoff.lab.tripAPI.security.filtering.JwtFilter;
import ru.tinkoff.lab.tripAPI.security.filtering.SecurityConfig;
import ru.tinkoff.lab.tripAPI.security.models.LoginRequest;
import ru.tinkoff.lab.tripAPI.security.utils.JwtProvider;
import ru.tinkoff.lab.tripAPI.security.utils.JwtUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = {RequestController.class, NotificationController.class, LoginController.class, UserController.class})
@Import({UserService.class, UuidTypeHandler.class, NotificationService.class, RequestService.class, AccommodationDestinationTripService.class,
        JwtUtils.class, JwtProvider.class, JwtFilter.class, SecurityConfig.class, AuthService.class, OfficeService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class RequestControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RequestService requestService;

    @Autowired
    OfficeService officeService;

    @Autowired
    UserService userService;

    @Autowired
    AccommodationDestinationTripService accommodationDestinationTripService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    RequestDto requestDto = new RequestDto("Just a request", "Nothing",
            new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2020 - 1901, 12, 15, 15, 0, 0, 0),
            "https:/somesite.com/JAOwe7IW78daAw1idh");

    Office office = new Office("Avenue 10", "Cool office");
    DestinationDto destinationDto = new DestinationDto("Zarechnaya 7", "9");

    Id workerId;

    Id approverId;

    Id approver2Id;

    Id requestId;

    String workerJwt;
    String approverJwt;
    String approver2Jwt;
    String adminJwt;

    @BeforeAll
    public void init() throws Exception {
        User worker = new User(
                "rs_xdm@inst.com",
                "qwertyuiop",
                "Ruslan",
                "Sultanov",
                "USER"
        );
        User approver = new User(
                "rs_xdm2@inst.com",
                "qwertyuiop",
                "Ruslan",
                "Sultanov",
                "USER"
        );
        User approver2 = new User(
                "rs_xdm3@inst.com",
                "qwertyuiop",
                "Ruslan",
                "Sultanov",
                "USER"
        );
        User admin = new User(
                "rs_xdm3afawf@inst.com",
                "awfawfawf",
                "Ruslan",
                "Sultanov",
                "ADMIN"
        );
        // Initialization some data
        //getting admin jwt
        String tempPas = admin.getPassword();
        userService.createUser(admin);

        RequestBuilder requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(admin.getEmail(), tempPas)));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();
        adminJwt = responseBodyPost;

        Id officeId = officeService.createOffice(office);
        office.setId(officeId.getId());
        destinationDto.setOfficeId(officeId.getId());

        Id destinationId = accommodationDestinationTripService.createDestination(destinationDto);
        destinationDto.setId(destinationId.getId());

        requestBuilderPost = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(worker))
                .header("Authorization", "Bearer " + adminJwt);

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        workerId = mapper.readValue(responseBodyPost, Id.class);
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

        // approver user
        requestBuilderPost = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(approver))
                .header("Authorization", "Bearer " + adminJwt);

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        approverId = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(approverId);
        approver.setId(approverId.getId());

        //getting first approver's jwt token
        requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(approver.getEmail(), approver.getPassword())));

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();
        approverJwt = responseBodyPost;

        // approver user
        requestBuilderPost = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(approver2))
                .header("Authorization", "Bearer " + adminJwt);

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        approver2Id = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(approver2Id);
        approver2.setId(approver2Id.getId());

        //getting first approver's jwt token
        requestBuilderPost = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.TEXT_PLAIN)
                .content(mapper.writeValueAsString(new LoginRequest(approver2.getEmail(), approver2.getPassword())));

        mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        responseBodyPost = mvcResultPost.getResponse().getContentAsString();
        approver2Jwt = responseBodyPost;

        userService.createRelation(UUID.fromString(approverId.getId()), UUID.fromString(workerId.getId()));
        userService.createRelation(UUID.fromString(approver2Id.getId()), UUID.fromString(workerId.getId()));


        // Adding new data to requestDto
        requestDto.setWorkerId(workerId.getId());
        requestDto.setDestinationId(destinationDto.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created request gets returned + test notifications")
    public void testCreateGetRequest() throws Exception {

        // Testing post method
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders
                .post("/requests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(requestDto));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        requestId = mapper.readValue(responseBodyPost, Id.class);
        assertNotNull(requestId);
        requestDto.setId(requestId.getId());

        // Testing get method
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/requests/" + requestDto.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Request requestFromResponse = mapper.readValue(responseBodyGet, Request.class);
        assertNotNull(requestFromResponse);
        assertEquals(requestDto.getId(), requestFromResponse.getId());

        //test if notifications are created
        List<Notification> notifications1 = notificationService
                .getUnwatchedNotifications(UUID.fromString(approverId.getId()));
        assertEquals(1, notifications1.size());
        assertEquals(requestId.getId(), notifications1.get(0).getRequest().getId());
        assertEquals(approverId.getId(), notifications1.get(0).getUserId());

        List<Notification> notifications2 = notificationService
                .getUnwatchedNotifications(UUID.fromString(approver2Id.getId()));
        assertEquals(1, notifications2.size());
        assertEquals(requestId.getId(), notifications2.get(0).getRequest().getId());
        assertEquals(approver2Id.getId(), notifications2.get(0).getUserId());
    }

    @Test
    @Order(2)
    @DisplayName("Test if request get updated")
    public void testUpdateRequest() throws Exception {
        requestDto.setDescription("Updated");
        // Updating request
        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/requests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(requestDto));

        mockMvc.perform(requestBuilderPut);

        // Checking if request was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/requests/" + requestDto.getId())
                .header("Authorization", "Bearer " + workerJwt)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Request updatedRequest = mapper.readValue(responseBodyGet, Request.class);
        assertNotNull(updatedRequest);
        assertEquals("Updated", updatedRequest.getDescription());

        assertEquals(RequestStatus.PENDING, updatedRequest.getRequestStatus());

        //test if NEW notifications are created
        List<Notification> notifications1 = notificationService
                .getUnwatchedNotifications(UUID.fromString(approverId.getId()));
        assertEquals(1, notifications1.size());
        assertEquals(requestId.getId(), notifications1.get(0).getRequest().getId());
        assertEquals(approverId.getId(), notifications1.get(0).getUserId());

        List<Notification> notifications2 = notificationService
                .getUnwatchedNotifications(UUID.fromString(approver2Id.getId()));
        assertEquals(1, notifications2.size());
        assertEquals(requestId.getId(), notifications2.get(0).getRequest().getId());
        assertEquals(approver2Id.getId(), notifications2.get(0).getUserId());
    }

    @Test
    @Order(3)
    @DisplayName("Test notification controller marks notification as watched")
    public void testNotificationController() throws Exception {
        // TEST markAsWatched
        List<Notification> notifications1 = notificationService
                .getUnwatchedNotifications(UUID.fromString(approverId.getId()));
        assertEquals(1, notifications1.size());

        Notification notificationToWatch = notifications1.get(0);

        RequestBuilder requestBuilderPost = MockMvcRequestBuilders
                .put("/notifications/" + notificationToWatch.getId() + "/watch")
                .header("Authorization", "Bearer " + approverJwt);

        mockMvc.perform(requestBuilderPost);

        notifications1 = notificationService
                .getUnwatchedNotifications(UUID.fromString(approverId.getId()));
        assertEquals(0, notifications1.size());
    }

    @Test
    @Order(4)
    @DisplayName("Send request for editing test + decline request test")
    public void testSendForEditingAndDecline() throws Exception {
        Accommodation accommodation = new Accommodation("Zayarskaya 29",
                "http://notALocalHost/reservation/oewjfoi2j3f98p32fh2h382yh9832");
        Id accommodationId = accommodationDestinationTripService.createAccommodation(accommodation);
        accommodation.setId(accommodationId.getId());

        TripDto tripDto = new TripDto(null, accommodationId.getId(), destinationDto.getId(),
                requestId.getId());

        RequestStatusChangeDto requestStatusChangeDto =
                new RequestStatusChangeDto(approverId.getId(), tripDto, "Change it!!");

        // Send For Editing test
        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/requests/" + requestId.getId() + "/send-for-editing")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + approverJwt)
                .content(mapper.writeValueAsString(requestStatusChangeDto));

        mockMvc.perform(requestBuilderPut);

        Request requestFromDb = requestService.getRequest(UUID.fromString(requestId.getId()));
        assertEquals(RequestStatus.AWAIT_CHANGES, requestFromDb.getRequestStatus());

        // Decline test
        requestBuilderPut = MockMvcRequestBuilders
                .put("/requests/" + requestId.getId() + "/decline")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + approverJwt)
                .content(mapper.writeValueAsString(requestStatusChangeDto));
        mockMvc.perform(requestBuilderPut);

        requestFromDb = requestService.getRequest(UUID.fromString(requestId.getId()));
        assertEquals(RequestStatus.DECLINED, requestFromDb.getRequestStatus());
    }

    @Test
    @Order(5)
    @DisplayName("Update and check notification test")
    public void testUpdateAndCheckNotification() throws Exception {
        Request request = requestService.getRequest(UUID.fromString(requestDto.getId()));
        assertEquals(RequestStatus.DECLINED, request.getRequestStatus());
        assertNotNull(request.getApproverId());
        requestDto.setComment("New comment");

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/requests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + workerJwt)
                .content(mapper.writeValueAsString(requestDto));
        mockMvc.perform(requestBuilderPut);

        request = requestService.getRequest(UUID.fromString(requestDto.getId()));
        assertEquals(RequestStatus.PENDING, request.getRequestStatus());
        List<Notification> notifications = notificationService.getUnwatchedNotifications(UUID.fromString(request.getApproverId()));
        assertEquals(1, notifications.size());

        List<Notification> notificationsForSecondApprover = notificationService.getUnwatchedNotifications(UUID.fromString(approver2Id.getId()));
        assertEquals(1, notificationsForSecondApprover.size());
    }

    @Test
    @Order(6)
    @DisplayName("Test approve endpoint")
    public void testApproveRequest() throws Exception {
        // Creating TripDto and Accomodation
        Accommodation accommodation = new Accommodation("Zayarskaya 29",
                "http://notALocalHost/reservation/oewjfoi2j3f98p32fh2h382yh9832");
        Id accommodationId = accommodationDestinationTripService.createAccommodation(accommodation);
        accommodation.setId(accommodationId.getId());

        TripDto tripDto = new TripDto(null, accommodationId.getId(), destinationDto.getId(),
                requestId.getId());

        RequestStatusChangeDto requestStatusChangeDto =
                new RequestStatusChangeDto(approverId.getId(), tripDto, "Some comment");

        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/requests/" + requestId.getId() + "/approve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + approverJwt)
                .content(mapper.writeValueAsString(requestStatusChangeDto));

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderPut).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Id idFromResponse = mapper.readValue(responseBodyGet, Id.class);

        Request approvedRequest = requestService.getRequest(UUID.fromString(requestId.getId()));
        assertEquals(RequestStatus.APPROVED, approvedRequest.getRequestStatus());
        assertEquals(approverId.getId(), approvedRequest.getApproverId());

        Trip tripFromDb = accommodationDestinationTripService
                .getTrip(UUID.fromString(idFromResponse.getId()));

        assertNotNull(tripFromDb);
        assertEquals(tripDto.getRequestId(), tripFromDb.getRequestId());
        assertEquals(tripDto.getAccommodationId(), tripFromDb.getAccommodation().getId());
        assertEquals(tripDto.getDestinationId(), tripFromDb.getDestination().getId());
        assertEquals(TripStatus.PENDING, tripFromDb.getTripStatus());
    }

    @Test
    @Order(7)
    @DisplayName("Testing unauthorized user")
    public void testUnauthorizedAccess() throws Exception {
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/requests/" + requestDto.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilderGet).andExpect(status().isForbidden());
    }
}

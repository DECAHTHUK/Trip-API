package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.utils.PasswordEncoder;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({RequestService.class, AccommodationDestinationTripService.class,
        UserService.class, OfficeService.class, NotificationService.class, UuidTypeHandler.class, PasswordEncoder.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class RequestServiceTest {
    @Value("${pagination}")
    private int ROWS_AMOUNT;

    @Autowired
    RequestService requestService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    AccommodationDestinationTripService accommodationDestinationTripService;

    @Autowired
    UserService userService;

    @Autowired
    OfficeService officeService;

    Timestamp timestampStart;
    Timestamp timestampEnd;

    List<RequestDto> requestDtos;

    Id workerId;
    String workerEmail;
    Id approverId;

    Id approver2Id;

    @BeforeAll
    public void init() {
        timestampStart = new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0);
        timestampEnd = new Timestamp(2020 - 1901, 12, 15, 15, 0, 0, 0);
        requestDtos = List.of(
                new RequestDto( "Just a request", "Nothing",
                        timestampStart, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"),
                new RequestDto( "Request", "Nothing",
                        timestampStart, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"),
                new RequestDto( "Request 3", "Nothing",
                        timestampStart, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"),
                new RequestDto ("Request with late start date",
                        "Nothing", timestampEnd, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"),
                new RequestDto( "Request 5",
                        "Nothing", timestampStart, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"),
                new RequestDto( "Request 6",
                        "Nothing", timestampStart, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"),
                new RequestDto( "Request 7 with late start date",
                        "Nothing", timestampEnd, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh"));
        User user = new User("email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER",
                "something");
        workerId = userService.createUser(user);
        workerEmail = user.getEmail();

        User userApprover = new User("approver@mail.ru",
                "1234",
                "Sam",
                "Winchester",
                "USER",
                "SALT");
        approverId = userService.createUser(userApprover);
        User userApprover2 = new User("approver2@mail.ru",
                "1234",
                "Sam",
                "Winchester",
                "USER",
                "SALT");
        approver2Id = userService.createUser(userApprover2);
        userService.createRelation(UUID.fromString(approverId.getId()), UUID.fromString(workerId.getId()));
        userService.createRelation(UUID.fromString(approver2Id.getId()), UUID.fromString(workerId.getId()));

        Accommodation accommodation = new Accommodation(
                "Zolotenko 24",
                "booking.com/DEJDNkdsmdneuwij12893hd"
        );
        Id accommodationId = accommodationDestinationTripService.createAccommodation(accommodation);

        Office office = new Office("Street 15", "Fine");
        Id officeId = officeService.createOffice(office);
        DestinationDto destinationDto = new DestinationDto("Description", "Seat place 8");
        destinationDto.setOfficeId(officeId.getId());
        Id destinationId = accommodationDestinationTripService.createDestination(destinationDto);

        for (RequestDto requestDto : requestDtos) {
            requestDto.setDestinationId(destinationId.getId());
            requestDto.setWorkerId(workerId.getId());
            Id requestId = requestService.createRequest(requestDto);
            requestDto.setId(requestId.getId());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created request is getting from db")
    public void getRequestTest() {
        Request requestFromDb = requestService.getRequest(UUID.fromString(requestDtos.get(0).getId()));
        User userFromDb = userService.findById(UUID.fromString(requestDtos.get(0).getWorkerId()));

        assertEquals(requestFromDb.getId(), requestDtos.get(0).getId());
        assertEquals(requestFromDb.getRequestStatus(), RequestStatus.PENDING);
        assertEquals(requestFromDb.getComment(), requestDtos.get(0).getComment());
        assertEquals(requestFromDb.getDescription(), requestDtos.get(0).getDescription());
        assertEquals(requestFromDb.getEndDate(), requestDtos.get(0).getEndDate());
        assertEquals(requestFromDb.getTicketsUrl(), requestDtos.get(0).getTicketsUrl());
        assertEquals(requestFromDb.getWorkerFirstName(), userFromDb.getFirstName());
        assertEquals(requestFromDb.getWorkerSecondName(), userFromDb.getSecondName());
        assertEquals(requestFromDb.getWorkerEmail(), userFromDb.getEmail());
        assertEquals(requestFromDb.getStartDate(), requestDtos.get(0).getStartDate());

        System.out.println(notificationService.getUnwatchedNotifications(UUID.fromString(approverId.getId())));
        System.out.println(notificationService.getUnwatchedNotifications(UUID.fromString(approver2Id.getId())));

    }

    @Test
    @Order(2)
    @DisplayName("Test if request is getting updated")
    public void updateRequestTest() {
        requestDtos.get(0).setComment("Updated comment");
        requestService.updateRequest(requestDtos.get(0));

        Request newRequest = requestService.getRequest(UUID.fromString(requestDtos.get(0).getId()));
        assertEquals("Updated comment", newRequest.getComment());
    }

    @Test
    @Order(3)
    @Disabled // no need for this test, because there are no cases, when we need to delete requests
    @DisplayName("Test if request is getting deleted")
    public void deleteRequestTest() {
        assertNotNull(requestService.getRequest(UUID.fromString(requestDtos.get(0).getId())));

        requestService.deleteRequest(UUID.fromString(requestDtos.get(0).getId()));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> requestService.getRequest(UUID.fromString(requestDtos.get(0).getId())));

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Test get incoming requests")
    public void getIncomingRequestsTest() {
        List<Request> requests = requestService.getIncomingRequests(UUID.fromString(approverId.getId()), 1);

        assertEquals(ROWS_AMOUNT, requests.size());

        List<Request> requests2 = requestService.getIncomingRequests(UUID.fromString(approverId.getId()), 2);

        assertEquals(2, requests2.size());
        assertTrue(requests2.stream().map(Request::getDescription)
                .anyMatch(t -> t.equals("Request 7 with late start date")));

        requests.addAll(requests2);
        assertTrue(requests.stream().map(Request::getDescription)
                .noneMatch(t -> t.equals("Request with await")));
    }

    @Test
    @Order(5)
    @DisplayName("Test get outgoing requests")
    public void getOutgoingRequestsTest() {
        List<Request> requests = requestService.getOutgoingRequests(UUID.fromString(workerId.getId()), 1);

        assertEquals(ROWS_AMOUNT, requests.size());

        List<Request> requests2 = requestService.getOutgoingRequests(UUID.fromString(workerId.getId()), 2);

        requests.addAll(requests2);

        assertTrue(requests.stream()
                .allMatch(t -> t.getWorkerEmail().equals(workerEmail)));
    }
}

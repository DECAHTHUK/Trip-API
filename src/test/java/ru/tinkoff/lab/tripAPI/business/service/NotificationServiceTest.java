package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.utils.PasswordEncoder;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({NotificationService.class, OfficeService.class, RequestService.class,
        UserService.class, AccommodationDestinationTripService.class, UuidTypeHandler.class, PasswordEncoder.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    RequestService requestService;

    @Autowired
    AccommodationDestinationTripService accommodationDestinationTripService;

    @Autowired
    UserService userService;

    @Autowired
    OfficeService officeService;

    NotificationDto notificationDto;
    DestinationDto destinationDto;
    Office office;

    Timestamp timestampStart;
    Timestamp timestampEnd;

    Integer unwatchedNotifications = 0;

    Id workerId;
    Id bossId;

    RequestDto firstRequest, secondRequest;

    @BeforeAll
    public void init() {
        notificationDto = new NotificationDto();
        timestampStart = new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0);
        timestampEnd = new Timestamp(2020 - 1901, 12, 15, 15, 0, 0, 0);
        User user = new User("email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER",
                "something");
        workerId = userService.createUser(user);
        user.setId(workerId.getId());

        User userBoss = new User("subordinate@mail.ru",
                "1234",
                "Sam",
                "Winchester",
                "USER",
                "SALT");
        bossId = userService.createUser(userBoss);
        userBoss.setId(bossId.getId());
        userService.createRelation(UUID.fromString(bossId.getId()), UUID.fromString(workerId.getId()));

        firstRequest = new RequestDto("Request 7 with late start date",
                "Nothing", timestampEnd, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh");

        firstRequest.setWorkerId(user.getId());
        office = new Office("Street 15", "Fine");
        Id officeId = officeService.createOffice(office);
        office.setId(officeId.getId());
        destinationDto = new DestinationDto("Description", "Seat place 8");
        destinationDto.setOfficeId(office.getId());
        Id destinationId = accommodationDestinationTripService.createDestination(destinationDto);
        destinationDto.setId(destinationId.getId());
        firstRequest.setDestinationId(destinationDto.getId());

        secondRequest = new RequestDto("Request 2",
                "Nothing", timestampEnd, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh");
        secondRequest.setWorkerId(user.getId());
        secondRequest.setDestinationId(destinationDto.getId());
        Id secondRequestId = requestService.createRequest(secondRequest);
        secondRequest.setId(secondRequestId.getId());

        Id firstRequestId = requestService.createRequest(firstRequest);
        firstRequest.setId(firstRequestId.getId());

        notificationDto.setRequestId(firstRequestId.getId());
        notificationDto.setUserId(userBoss.getId());
        Id notificationId = notificationService.createNotification(notificationDto);
        notificationDto.setId(notificationId.getId());

        notificationDto.setRequestId(secondRequestId.getId());
        notificationDto.setUserId(userBoss.getId());
        Id notificationId2 = notificationService.createNotification(notificationDto);
        notificationDto.setId(notificationId2.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if notifications are getting returned")
    public void getUnwatchedNotificationsTest() {
        List<Notification> notifications = notificationService.getUnwatchedNotifications(UUID.fromString(bossId.getId()));
        unwatchedNotifications = notifications.size();
        for (Notification notification : notifications)
            System.out.println(notification);
        assertNotNull(notifications);
        assertEquals(unwatchedNotifications, notifications.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test if notification is getting updated")
    public void updateNotificationTest() {
        notificationDto.setWatched(true);
        unwatchedNotifications--;
        notificationService.updateNotification(notificationDto);
        List<Notification> notifications = notificationService.getUnwatchedNotifications(UUID.fromString(bossId.getId()));
        assertEquals(unwatchedNotifications, notifications.size());
    }

    @Test
    @Order(3)
    @DisplayName("Test get notification by its id")
    public void getNotificationTest() {
        Notification notification = notificationService.getNotificationById(UUID.fromString(notificationDto.getId()));
        assertNotNull(notification);
    }

    @Test
    @Order(4)
    @DisplayName("Test if notification is getting deleted")
    public void deleteNotificationTest() {
        notificationDto.setWatched(false);
        notificationService.deleteNotification(UUID.fromString(notificationDto.getId()));
        List<Notification> notifications = notificationService.getUnwatchedNotifications(UUID.fromString(bossId.getId()));
        assertEquals(unwatchedNotifications, notifications.size());
    }
}
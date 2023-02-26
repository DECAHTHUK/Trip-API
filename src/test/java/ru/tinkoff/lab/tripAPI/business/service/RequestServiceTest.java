package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({RequestService.class, AccommodationDestinationTripService.class,
        UserService.class, OfficeService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestServiceTest {
    @Autowired
    RequestService requestService;

    @Autowired
    AccommodationDestinationTripService accommodationDestinationTripService;

    @Autowired
    UserService userService;

    @Autowired
    OfficeService officeService;

    RequestDto requestDto = new RequestDto(RequestStatus.PENDING, "Just a request", "Nothing",
            new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2020 - 1901, 12, 15, 15, 0, 0, 0),
            "New Orlean", "Chebarkyl", "https:/somesite.com/JAOwe7IW78daAw1idh");

    @BeforeAll
    public void init() {
        User user = new User("email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER",
                "salt");
        Id workerId = userService.createUser(user);

        Accommodation accommodation = new Accommodation(
                "Zolotenko 24",
                new Timestamp(2022 - 1901, 12, 12, 12, 0, 0, 0),
                new Timestamp(2022 - 1901, 12, 15, 15, 0, 0, 0),
                "booking.com/DEJDNkdsmdneuwij12893hd"
        );
        Id accommodationId = accommodationDestinationTripService.createAccommodation(accommodation);

        Office office = new Office("Street 15", "Fine");
        Id officeId = officeService.createOffice(office);
        DestinationDto destinationDto = new DestinationDto("Description", "Seat place 8");
        destinationDto.setOfficeId(officeId.getId());
        Id destinationId = accommodationDestinationTripService.createDestination(destinationDto);

        TripDto tripDto = new TripDto(TripStatus.PENDING, accommodationId.getId(), destinationId.getId());
        Id tripId = accommodationDestinationTripService.createTrip(tripDto);

        requestDto.setOfficeId(officeId.getId());
        requestDto.setTripId(tripId.getId());
        requestDto.setWorkerId(workerId.getId());
        Id requestId = requestService.createRequest(requestDto);
        requestDto.setId(requestId.getId());
    }

    @Test
    @Order(1)
    @DisplayName("")
    public void getRequestTest() {
        Request requestFromDb = requestService.getRequest(requestDto.getId());
        System.out.println(requestFromDb);
    }
}

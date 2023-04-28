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
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;
import ru.tinkoff.lab.tripAPI.security.utils.JwtUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({AccommodationDestinationTripService.class, OfficeService.class, RequestService.class, JwtUtils.class,
        UserService.class, NotificationService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class AccommodationDestinationTripServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    AccommodationDestinationTripService accommodationDestinationTripService;

    @Autowired
    OfficeService officeService;

    @Autowired
    RequestService requestService;

    DestinationDto destinationDto;
    Office office1;
    Office office2;

    TripDto tripDto;
    Accommodation accommodation1;

    Accommodation accommodation2;

    RequestDto requestDto;

    Timestamp timestampStart;
    Timestamp timestampEnd;

    User worker;

    @BeforeAll
    public void init() {
        timestampStart = new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0);
        timestampEnd = new Timestamp(2020 - 1901, 12, 15, 15, 0, 0, 0);

        destinationDto = new DestinationDto("Description", "Seat place 8");
        tripDto = new TripDto();
        office1 = new Office("Street 15", "Fine");
        office2 = new Office("Street new", "Updated");
        accommodation1 = new Accommodation(
                "Zolotenko 24",
                "booking.com/DEJDNkdsmdneuwij12893hd"
        );
        accommodation2 = new Accommodation(
                "Updated address",
                "booking.com/DEJDNkdsmdneuwij12893hd"
        );
        worker = new User("email@mail.ru",
                "12345678",
                "John",
                "Smith",
                "USER");
        requestDto = new RequestDto("Pending request",
                "Nothing", timestampEnd, timestampEnd, "https:/somesite.com/JAOwe7IW78daAw1idh");
        // init accommodations
        Id accommodationId1 = accommodationDestinationTripService.createAccommodation(accommodation1);
        accommodation1.setId(accommodationId1.getId());
        Id accommodationId2 = accommodationDestinationTripService.createAccommodation(accommodation2);
        accommodation2.setId(accommodationId2.getId());

        //init offices
        office1.setId(officeService.createOffice(office1).getId());
        office2.setId(officeService.createOffice(office2).getId());

        //init destination
        destinationDto.setOfficeId(office1.getId());
        Id destinationId = accommodationDestinationTripService.createDestination(destinationDto);
        destinationDto.setId(destinationId.getId());

        Id workerId = userService.createUser(worker);
        worker.setId(workerId.getId());

        requestDto.setWorkerId(worker.getId());
        requestDto.setDestinationId(destinationId.getId());

        Id requestId = requestService.createRequest(requestDto);
        requestDto.setId(requestId.getId());

        //init trip
        tripDto.setAccommodationId(accommodationId1.getId());
        tripDto.setDestinationId(destinationId.getId());
        tripDto.setTripStatus(TripStatus.COMPLETED);
        tripDto.setRequestId(requestDto.getId());
        tripDto.setId(accommodationDestinationTripService.createTrip(tripDto).getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created accommodation is returned from db")
    public void getAccommodationTest() {
        Accommodation accommodationFromDB = accommodationDestinationTripService.getAccommodation(UUID.fromString(accommodation1.getId()));

        assertEquals(accommodation1.getAddress(), accommodationFromDB.getAddress());
        assertEquals(accommodation1.getBookingUrl(), accommodationFromDB.getBookingUrl());
    }

    @Test
    @Order(2)
    @DisplayName("Test if accommodation is getting updated")
    public void testUpdateAccommodation_shouldChangeValuesInDb() {
        accommodation1.setAddress("updated");
        accommodationDestinationTripService.updateAccommodation(accommodation1);

        Accommodation newAccom = accommodationDestinationTripService.getAccommodation(UUID.fromString(accommodation1.getId()));
        assertEquals("updated", newAccom.getAddress());
        accommodation1.setAddress("Zolotenko 24");
    }

    @Test
    @Order(3)
    @DisplayName("Test if newly created destination is returned from db")
    public void getDestinationTest() {
        Destination destination = accommodationDestinationTripService.getDestination(UUID.fromString(destinationDto.getId()));

        assertEquals(destination.getDescription(), destinationDto.getDescription());
        assertEquals(destination.getOffice().getId(), destinationDto.getOfficeId());
        assertEquals(destination.getSeatPlace(), destinationDto.getSeatPlace());
    }

    @Test
    @Order(4)
    @DisplayName("Test if destination is getting updated")
    public void testUpdateDestination_shouldChangeOffices() {
        Destination destinationFromDb =
                accommodationDestinationTripService.getDestination(UUID.fromString(destinationDto.getId()));

        Office officeFromDestination = destinationFromDb.getOffice();

        destinationDto.setOfficeId(office2.getId());
        accommodationDestinationTripService.updateDestination(destinationDto);

        destinationFromDb =
                accommodationDestinationTripService.getDestination(UUID.fromString(destinationDto.getId()));

        Office officeFromDestinationUpdated = destinationFromDb.getOffice();

        assertEquals(office2, officeFromDestinationUpdated);
        assertNotEquals(officeFromDestination.getDescription(), officeFromDestinationUpdated.getDescription());
    }

    @Test
    @Order(5)
    @DisplayName("Test if newly created trip is returned from db")
    public void testGetTrip() {
        Trip tripFromDb = accommodationDestinationTripService.getTrip(UUID.fromString(tripDto.getId()));
        System.out.println(tripFromDb);

        assertEquals(accommodationDestinationTripService.getDestination(UUID.fromString(tripDto.getDestinationId())),
                tripFromDb.getDestination());
        assertEquals(accommodationDestinationTripService.getAccommodation(UUID.fromString(tripDto.getAccommodationId())),
                tripFromDb.getAccommodation());
        assertEquals(tripDto.getRequestId(), tripFromDb.getRequestId());
        assertEquals(tripDto.getTripStatus(), tripFromDb.getTripStatus());

        List<Trip> trips = accommodationDestinationTripService.getSomeTrips(UUID.fromString(worker.getId()), 1, null);
        assertEquals(1, trips.size());
    }

    @Test
    @Order(6)
    @DisplayName("Test if trip is getting updated")
    public void testUpdateTrip_shouldChangeAccommodationAndStatus() {
        Trip tripFromDb = accommodationDestinationTripService.getTrip(UUID.fromString(tripDto.getId()));

        Accommodation accommodationFromTrip = tripFromDb.getAccommodation();
        Accommodation accommodation = accommodationDestinationTripService.getAccommodation(UUID.fromString(accommodation1.getId()));
        System.out.println(accommodation);
        assertEquals(accommodation1, accommodationFromTrip);

        TripStatus statusFromTrip = tripFromDb.getTripStatus();
        assertEquals(tripDto.getTripStatus(), statusFromTrip);

        tripDto.setAccommodationId(accommodation2.getId());
        tripDto.setTripStatus(TripStatus.valueOf("PENDING"));
        accommodationDestinationTripService.updateTrip(tripDto);

        tripFromDb = accommodationDestinationTripService.getTrip(UUID.fromString(tripDto.getId()));

        assertEquals(accommodation2, tripFromDb.getAccommodation());
        assertEquals(TripStatus.PENDING, tripFromDb.getTripStatus());
    }
}

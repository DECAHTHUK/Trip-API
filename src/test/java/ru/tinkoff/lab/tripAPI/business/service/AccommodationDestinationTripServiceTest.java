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
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({AccommodationDestinationTripService.class, OfficeService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccommodationDestinationTripServiceTest {

    @Autowired
    AccommodationDestinationTripService accommodationDestinationTripService;

    @Autowired
    OfficeService officeService;

    DestinationDto destinationDto = new DestinationDto("Description", "Seat place 8");
    Office office1 = new Office("Street 15", "Fine");
    Office office2 = new Office("Street new", "Updated");

    TripDto tripDto = new TripDto();

    //TODO ask about this timestamp quirk
    Accommodation accommodation1 = new Accommodation(
            "Zolotenko 24",
            new Timestamp(2022 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2022 - 1901, 12, 15, 15, 0, 0, 0),
            "booking.com/DEJDNkdsmdneuwij12893hd"
    );

    Accommodation accommodation2 = new Accommodation(
            "Updated address",
            new Timestamp(2022 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2022 - 1901, 12, 15, 15, 0, 0, 0),
            "booking.com/DEJDNkdsmdneuwij12893hd"
    );

    @BeforeAll
    public void init() {
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

        //init trip
        tripDto.setAccommodationId(accommodationId1.getId());
        tripDto.setDestinationId(destinationId.getId());
        tripDto.setTripStatus(TripStatus.COMPLETED);
        tripDto.setId(accommodationDestinationTripService.createTrip(tripDto).getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created accommodation is returned from db")
    public void getAccommodationTest() {
        Accommodation accommodationFromDB = accommodationDestinationTripService.getAccommodation(accommodation1.getId());

        assertEquals(accommodation1.getAddress(), accommodationFromDB.getAddress());
        assertEquals(accommodation1.getCheckinTime(), accommodationFromDB.getCheckinTime());
        assertEquals(accommodation1.getCheckoutTime(), accommodationFromDB.getCheckoutTime());
        assertEquals(accommodation1.getBookingUrl(), accommodationFromDB.getBookingUrl());
    }

    @Test
    @Order(2)
    @DisplayName("Test if accommodation is getting updated")
    public void testUpdateAccommodation_shouldChangeValuesInDb() {
        accommodation1.setAddress("updated");
        accommodationDestinationTripService.updateAccommodation(accommodation1);

        Accommodation newAccom = accommodationDestinationTripService.getAccommodation(accommodation1.getId());
        assertEquals("updated", newAccom.getAddress());
        accommodation1.setAddress("Zolotenko 24");
    }

    @Test
    @Order(3)
    @DisplayName("Test if newly created destination is returned from db")
    public void getDestinationTest() {
        Destination destination = accommodationDestinationTripService.getDestination(destinationDto.getId());

        assertEquals(destination.getDescription(), destinationDto.getDescription());
        assertEquals(destination.getOffice().getId(), destinationDto.getOfficeId());
        assertEquals(destination.getSeatPlace(), destinationDto.getSeatPlace());
    }

    @Test
    @Order(4)
    @DisplayName("Test if destination is getting updated")
    public void testUpdateDestination_shouldChangeOffices() {
        Destination destinationFromDb =
                accommodationDestinationTripService.getDestination(destinationDto.getId());

        Office officeFromDestination = destinationFromDb.getOffice();

        destinationDto.setOfficeId(office2.getId());
        accommodationDestinationTripService.updateDestination(destinationDto);

        destinationFromDb =
                accommodationDestinationTripService.getDestination(destinationDto.getId());

        Office officeFromDestinationUpdated = destinationFromDb.getOffice();

        assertEquals(office2, officeFromDestinationUpdated);
        assertNotEquals(officeFromDestination.getDescription(), officeFromDestinationUpdated.getDescription());
    }

    @Test
    @Order(5)
    @DisplayName("Test if newly created trip is returned from db")
    public void testGetTrip() {
        Trip tripFromDb = accommodationDestinationTripService.getTrip(tripDto.getId());

        assertEquals(accommodationDestinationTripService.getDestination(tripDto.getDestinationId()),
                tripFromDb.getDestination());
        assertEquals(accommodationDestinationTripService.getAccommodation(tripDto.getAccommodationId()),
                tripFromDb.getAccommodation());
        assertEquals(tripDto.getTripStatus(), tripFromDb.getTripStatus());
    }

    @Test
    @Order(6)
    @DisplayName("Test if trip is getting updated")
    public void testUpdateTrip_shouldChangeAccommodationAndStatus() {
        Trip tripFromDb = accommodationDestinationTripService.getTrip(tripDto.getId());

        Accommodation accommodationFromTrip = tripFromDb.getAccommodation();
        Accommodation accommodation = accommodationDestinationTripService.getAccommodation(accommodation1.getId());
        System.out.println(accommodation);
        assertEquals(accommodation1, accommodationFromTrip);

        TripStatus statusFromTrip = tripFromDb.getTripStatus();
        assertEquals(tripDto.getTripStatus(), statusFromTrip);

        tripDto.setAccommodationId(accommodation2.getId());
        tripDto.setTripStatus(TripStatus.valueOf("PENDING"));
        accommodationDestinationTripService.updateTrip(tripDto);

        tripFromDb = accommodationDestinationTripService.getTrip(tripDto.getId());

        assertEquals(accommodation2, tripFromDb.getAccommodation());
        assertEquals(TripStatus.PENDING, tripFromDb.getTripStatus());
    }
}
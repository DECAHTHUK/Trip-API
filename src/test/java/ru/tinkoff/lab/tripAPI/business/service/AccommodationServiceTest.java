package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({AccommodationService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccommodationServiceTest {

    @Autowired
    AccommodationService accommodationService;

    Accommodation accommodation = new Accommodation(
            "Zolotenko 24",
            new Timestamp(2022, 12, 12, 12, 0, 0, 0),
            new Timestamp(2022, 12, 15, 15, 0, 0, 0),
            "booking.com/DEJDNkdsmdneuwij12893hd"
    );

    @BeforeAll
    public void createAccommodation() {
        Id accommodationId = accommodationService.createAccommodation(accommodation);
        accommodation.setId(accommodationId.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created accommodation gets from db")
    public void getAccommodationTest() {
        Accommodation accommodationFromDB = accommodationService.getAccommodation(accommodation.getId());

        assertEquals(accommodation.getAddress(), accommodationFromDB.getAddress());
        assertEquals(accommodation.getCheckinTime(), accommodationFromDB.getCheckinTime());
        assertEquals(accommodation.getCheckoutTime(), accommodationFromDB.getCheckoutTime());
        assertEquals(accommodation.getBookingUrl(), accommodationFromDB.getBookingUrl());
    }
}

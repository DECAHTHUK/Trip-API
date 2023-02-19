package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({DestinationService.class, OfficeService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DestinationServiceTest {

    @Autowired
    DestinationService destinationService;

    @Autowired
    OfficeService officeService;

    DestinationDto destinationDto = new DestinationDto("Description", "Seat place 8");
    Office office = new Office("Street 15", "Fine");

    @BeforeAll
    public void createDestination() {
        Id officeId = officeService.createOffice(office);
        office.setId(officeId.getId());
        destinationDto.setOfficeId(officeId.getId());
        Id destinationId = destinationService.createDestination(destinationDto);
        destinationDto.setId(destinationId.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created destination gets from db")
    public void getDestinationTest() {
        Destination destination = destinationService.getDestination(destinationDto.getId());

        assertEquals(destination.getDescription(), destinationDto.getDescription());
        assertEquals(destination.getOffice().getId(), destinationDto.getOfficeId());
        assertEquals(destination.getSeatPlace(), destinationDto.getSeatPlace());
    }
}

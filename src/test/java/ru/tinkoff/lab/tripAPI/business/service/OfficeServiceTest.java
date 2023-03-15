package ru.tinkoff.lab.tripAPI.business.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({OfficeService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class OfficeServiceTest {

    @Autowired
    OfficeService officeService;

    Office office;

    @BeforeAll
    public void createOffice() {
        office = new Office("Lenina 78", "Cool office from Tinkoff");
        Id officeId = officeService.createOffice(office);
        office.setId(officeId.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Test if newly created office gets returned from db")
    public void getOfficeTest() {
        Office officeFromDB = officeService.getOffice(UUID.fromString(office.getId()));
        assertEquals(office.getAddress(), officeFromDB.getAddress());
        assertEquals(office.getDescription(), officeFromDB.getDescription());
    }

    @Test
    @Order(2)
    @DisplayName("Test if office in the db is getting updated")
    public void updateOfficeTest() {
        office.setAddress("Revolution Square 17");
        office.setDescription("lalala");
        officeService.updateOffice(office);

        Office updatedOffice = officeService.getOffice(UUID.fromString(office.getId()));

        assertEquals(office.getAddress(), updatedOffice.getAddress());
        assertEquals(office.getDescription(), updatedOffice.getDescription());
    }

    @Test
    @Order(3)
    @DisplayName("Test office is getting deleted")
    public void deleteOfficeTest() {
        assertNotNull(officeService.getOffice(UUID.fromString(office.getId())));

        officeService.deleteOffice(UUID.fromString(office.getId()));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> officeService.getOffice(UUID.fromString(office.getId())));

        assertEquals(HttpStatus.NOT_FOUND + " \"Office with id = " + office.getId() + " was not found\"", thrown.getMessage());
    }
}

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.business.service.AccommodationDestinationTripService;
import ru.tinkoff.lab.tripAPI.business.service.OfficeService;
import ru.tinkoff.lab.tripAPI.business.service.RequestService;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.mapping.handlers.UuidTypeHandler;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@AutoConfigureMybatis
@WebMvcTest(controllers = RequestController.class)
@Import({RequestService.class, OfficeService.class, UserService.class,
        AccommodationDestinationTripService.class, UuidTypeHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestControllerTest {

    @Autowired
    OfficeService officeService;

    @Autowired
    UserService userService;

    @Autowired
    AccommodationDestinationTripService service;

    @Autowired
    private MockMvc mockMvc;

    RequestDto requestDto = new RequestDto(RequestStatus.PENDING, "Just a request", "Nothing",
            new Timestamp(2020 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2020 - 1901, 12, 15, 15, 0, 0, 0),
            "New Orlean", "Chebarkyl", "https:/somesite.com/JAOwe7IW78daAw1idh");

    Accommodation accommodation = new Accommodation(
            "Republic street 28",
            new Timestamp(2022 - 1901, 12, 12, 12, 0, 0, 0),
            new Timestamp(2022 - 1901, 12, 15, 15, 0, 0, 0),
            "booking.com/DEJDNkdsmdneuwij12893hd"
    );
    Office office = new Office("Avenue 10", "Cool office");
    DestinationDto destinationDto = new DestinationDto("Zarechnaya 7", "9");
    TripDto tripDto = new TripDto();

    User worker = new User(
            "rs_xdm@inst.com",
            "qwertyuiop",
            "Ruslan",
            "Sultanov",
            "user"
    );

    @Test
    @Order(1)
    @DisplayName("Test if newly created request gets returned")
    public void testCreateGetRequest() throws Exception {
        // Initialization some data
        Id officeId = officeService.createOffice(office);
        office.setId(officeId.getId());
        destinationDto.setOfficeId(officeId.getId());
        Id destinationId = service.createDestination(destinationDto);
        destinationDto.setId(destinationId.getId());
        Id accommodationId = service.createAccommodation(accommodation);
        accommodation.setId(accommodationId.getId());
        tripDto.setTripStatus(TripStatus.PENDING);
        tripDto.setDestinationId(destinationId.getId());
        tripDto.setAccommodationId(accommodationId.getId());
        Id tripId = service.createTrip(tripDto);
        tripDto.setId(tripId.getId());
        Id workerId = userService.createUser(worker);
        worker.setId(workerId.getId());
        // Adding new data to requestDto
        requestDto.setOfficeId(officeId.getId());
        requestDto.setTripId(tripId.getId());
        requestDto.setWorkerId(workerId.getId());

        // Testing post method
        RequestBuilder requestBuilderPost = MockMvcRequestBuilders
                .post("/requests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto));

        MvcResult mvcResultPost = mockMvc.perform(requestBuilderPost).andReturn();
        String responseBodyPost = mvcResultPost.getResponse().getContentAsString();

        Id requestId = new ObjectMapper().readValue(responseBodyPost, Id.class);
        assertNotNull(requestId);
        requestDto.setId(requestId.getId());

        // Testing get method
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/requests/" + requestDto.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Request requestFromResponse = new ObjectMapper().readValue(responseBodyGet, Request.class);
        assertNotNull(requestFromResponse);
        assertEquals(requestDto.getId(), requestFromResponse.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Test if request get updated and deleted")
    public void testUpdateDeleteRequest() throws Exception {
        requestDto.setRequestStatus(RequestStatus.APPROVED);
        // Updating request
        RequestBuilder requestBuilderPut = MockMvcRequestBuilders
                .put("/requests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDto));

        mockMvc.perform(requestBuilderPut);

        // Checking if request was updated
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders
                .get("/requests/" + requestDto.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        String responseBodyGet = mvcResultGet.getResponse().getContentAsString();

        Request updatedRequest = new ObjectMapper().readValue(responseBodyGet, Request.class);
        assertNotNull(updatedRequest);
        assertEquals(RequestStatus.APPROVED, updatedRequest.getRequestStatus());

        // Deleting request
        RequestBuilder requestBuilderDelete = MockMvcRequestBuilders
                .delete("/requests/" + requestDto.getId());

        mockMvc.perform(requestBuilderDelete);

        mvcResultGet = mockMvc.perform(requestBuilderGet).andReturn();
        ResponseStatusException exception = (ResponseStatusException) mvcResultGet.getResolvedException();

        assert exception != null;
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}

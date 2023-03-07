package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("/application.yaml")
public class AccommodationDestinationTripService {

    private final AccommodationDestinationTripMapper mapper;

    @Value("${pagination}")
    private int ROWS_AMOUNT;

    /**
     * Accommodation service
     */
    public Id createAccommodation(Accommodation accommodation) {
        try {
            return mapper.insertAccommodation(accommodation);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void deleteAccommodation(String uuid) {
        mapper.deleteAccommodation(UUID.fromString(uuid));
    }

    public Accommodation getAccommodation(String uuid) {
        Accommodation accommodation = mapper.selectAccommodation(UUID.fromString(uuid));
        if (accommodation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accommodation with id = " + uuid + " was not found");
        }
        return accommodation;
    }

    public void updateAccommodation(Accommodation accommodation) {
        mapper.updateAccommodation(accommodation);
    }

    /**
     * Destination service
     */
    public Id createDestination(DestinationDto destinationDto) {
        try {
            return mapper.insertDestination(destinationDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Destination getDestination(String uuid) {
        Destination destination = mapper.selectDestination(UUID.fromString(uuid));
        if (destination == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination with id = " + uuid + " was not found");
        }
        return destination;
    }

    public void deleteDestination(String uuid) {
        mapper.deleteDestination(UUID.fromString(uuid));
    }

    public void updateDestination(DestinationDto destinationDto) {
        mapper.updateDestination(destinationDto);
    }

    /**
     * Trip service
     */
    public Id createTrip(TripDto tripDto) {
        try {
            return mapper.insertTrip(tripDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Trip getTrip(String uuid) {
        Trip trip = mapper.selectTrip(UUID.fromString(uuid));
        if (trip == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip with id = " + uuid + " was not found");
        }
        return trip;
    }

    public List<Trip> getSomeTrips(String userId, int page) {
        return mapper.selectSomeTrips(UUID.fromString(userId), page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    public void updateTrip(TripDto tripDto) {
        mapper.updateTrip(tripDto);
    }

    public void deleteTrip(String uuid) {
        mapper.deleteTrip(UUID.fromString(uuid));
    }
}
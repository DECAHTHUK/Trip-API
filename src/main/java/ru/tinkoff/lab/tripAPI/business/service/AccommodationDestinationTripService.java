package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.exceptions.*;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
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
        } catch (Exception e) {
            throw new AccommodationCreateException(e.getMessage());
        }
    }

    public void deleteAccommodation(UUID uuid) {
        mapper.deleteAccommodation(uuid);
    }

    public Accommodation getAccommodation(UUID uuid) {
        Accommodation accommodation = mapper.selectAccommodation(uuid);
        if (accommodation == null) {
            throw new AccommodationNotFoundException("Accommodation with id = " + uuid + " was not found");
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
        } catch (DestinationCreateException e) {
            throw new DestinationCreateException(e.getMessage());
        }
    }

    public Destination getDestination(UUID uuid) {
        Destination destination = mapper.selectDestination(uuid);
        if (destination == null) {
            throw new DestinationNotFoundException("Destination with id = " + uuid + " was not found");
        }
        return destination;
    }

    public void deleteDestination(UUID uuid) {
        mapper.deleteDestination(uuid);
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
        } catch (TripCreateException e) {
            throw new TripCreateException(e.getMessage());
        }
    }

    public Trip getTrip(UUID uuid) {
        Trip trip = mapper.selectTrip(uuid);
        if (trip == null) {
            throw new TripNotFoundException("Trip with id = " + uuid + " was not found");
        }
        return trip;
    }

    public List<Trip> getSomeTrips(UUID userId, int page) {
        return mapper.selectSomeTrips(userId, page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    public void updateTrip(TripDto tripDto) {
        mapper.updateTrip(tripDto);
    }

    public void cancelTrip(UUID uuid) {mapper.cancelTrip(uuid);}

    public void completeTrip(UUID uuid) {mapper.completeTrip(uuid);}

    public void deleteTrip(UUID uuid) {
        mapper.deleteTrip(uuid);
    }
}
package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccommodationDestinationTripService {

    private final AccommodationDestinationTripMapper mapper;

    /**
     * Accommodation service
     */
    public Id createAccommodation(Accommodation accommodation) {
        try {
            return mapper.insertAccommodation(accommodation);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void deleteAccommodation(String uuid) {
        mapper.deleteAccommodation(UUID.fromString(uuid));
    }

    public Accommodation getAccommodation(String uuid) {
        return mapper.selectAccommodation(UUID.fromString(uuid));
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
            return null;
        }
    }

    public Destination getDestination(String uuid) {
        return mapper.selectDestination(UUID.fromString(uuid));
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
            return null;
        }
    }

    public Trip getTrip(String uuid) {
        return mapper.selectTrip(UUID.fromString(uuid));
    }

    public void updateTrip(TripDto tripDto) {
        mapper.updateTrip(tripDto);
    }

    public void deleteTrip(String uuid) {
        mapper.deleteTrip(UUID.fromString(uuid));
    }
}
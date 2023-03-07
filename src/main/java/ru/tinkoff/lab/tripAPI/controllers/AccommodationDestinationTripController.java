package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Trip;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.service.AccommodationDestinationTripService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccommodationDestinationTripController {

    private final AccommodationDestinationTripService service;

    /**
     * Accommodation endpoints
     */
    @PostMapping(value = "/accommodations")
    public Id createNewAccommodation(@RequestBody Accommodation accommodation) {
        return service.createAccommodation(accommodation);
    }

    @DeleteMapping(value = "/accommodations/{uuid}")
    public void deleteAccommodation(@PathVariable UUID uuid) {
        service.deleteAccommodation(uuid);
    }

    @GetMapping(value = "/accommodations/{uuid}")
    public Accommodation getAccommodation(@PathVariable UUID uuid) {
        return service.getAccommodation(uuid);
    }

    @PutMapping(value = "/accommodations")
    public void updateAccommodation(@RequestBody Accommodation accommodation) {
        service.updateAccommodation(accommodation);
    }

    /**
     * Destination endpoints
     */
    @PostMapping("/destinations")
    public Id createNewDestination(@RequestBody DestinationDto destinationDto) {
        return service.createDestination(destinationDto);
    }

    @GetMapping("/destinations/{uuid}")
    public Destination getDestination(@PathVariable UUID uuid) {
        return service.getDestination(uuid);
    }

    @DeleteMapping("/destinations/{uuid}")
    public void deleteDestination(@PathVariable UUID uuid) {
        service.deleteDestination(uuid);
    }

    @PutMapping("/destinations")
    public void updateDestination(@RequestBody DestinationDto destinationDto) {
        service.updateDestination(destinationDto);
    }

    /**
     * Trip endpoints
     */
    @PostMapping("/trips")
    public Id createTrip(@RequestBody TripDto tripDto) {
        return service.createTrip(tripDto);
    }

    @GetMapping("/trips/{uuid}")
    public Trip getTrip(@PathVariable UUID uuid) {
        return service.getTrip(uuid);
    }

    @PutMapping("/trips")
    public void updateTrip(@RequestBody TripDto tripDto) {
        service.updateTrip(tripDto);
    }

    @DeleteMapping("/trips/{uuid}")
    public void deleteTrip(@PathVariable UUID uuid) {
        service.deleteTrip(uuid);
    }
}

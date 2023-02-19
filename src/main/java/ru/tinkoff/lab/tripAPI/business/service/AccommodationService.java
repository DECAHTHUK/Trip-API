package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationDestinationTripMapper accommodationMapper;

    public Id createAccommodation(Accommodation accommodation) {
        try {
            return accommodationMapper.insertAccommodation(accommodation);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void deleteAccommodation(String uuid) {
        accommodationMapper.deleteAccommodation(UUID.fromString(uuid));
    }

    public Accommodation getAccommodation(String uuid) {
        return accommodationMapper.selectAccommodation(UUID.fromString(uuid));
    }

    public void updateAccommodation(Accommodation accommodation) {
        accommodationMapper.updateAccommodation(accommodation);
    }
}
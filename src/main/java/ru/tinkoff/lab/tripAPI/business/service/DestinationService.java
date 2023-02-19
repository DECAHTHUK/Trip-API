package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.mapping.AccommodationDestinationTripMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final AccommodationDestinationTripMapper destinationMapper;

    public Id createDestination(DestinationDto destinationDto) {
        try {
            return destinationMapper.insertDestination(destinationDto);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public Destination getDestination(String uuid) {
        return destinationMapper.selectDestination(UUID.fromString(uuid));
    }

    public void deleteDestination(String uuid) {
        destinationMapper.deleteDestination(UUID.fromString(uuid));
    }

    public void updateDestination(DestinationDto destinationDto) {
        destinationMapper.updateDestination(destinationDto);
    }
}

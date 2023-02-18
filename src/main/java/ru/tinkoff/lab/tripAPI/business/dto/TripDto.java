package ru.tinkoff.lab.tripAPI.business.dto;

import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;

public class TripDto {
    private String id;

    private TripStatus tripStatus;

    private String accommodationId;

    private String destinationId;
}

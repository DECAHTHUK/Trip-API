package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;

@Data
public class Trip {

    private String id;

    private TripStatus trip_status;

    private Accommodation accommodation;

    private Destination destination;
}

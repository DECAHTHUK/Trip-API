package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;

@Data
@NoArgsConstructor
public class Trip {

    private String id;

    private TripStatus tripStatus;

    private Accommodation accommodation;

    private Destination destination;
}

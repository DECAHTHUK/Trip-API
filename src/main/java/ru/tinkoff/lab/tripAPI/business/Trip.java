package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Trip {
    private String id;

    private TripStatus tripStatus;

    private Accommodation accommodation;

    private Destination destination;

    private String requestId;

    private Timestamp startDate;

    private Timestamp endDate;
}

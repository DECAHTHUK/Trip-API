package ru.tinkoff.lab.tripAPI.business.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;

@Data
@NoArgsConstructor
public class TripDto {
    private String id;

    private TripStatus tripStatus;

    private String accommodationId;

    private String destinationId;

    public TripDto(TripStatus tripStatus, String accommodationId, String destinationId) {
        this.tripStatus = tripStatus;
        this.accommodationId = accommodationId;
        this.destinationId = destinationId;
    }
}

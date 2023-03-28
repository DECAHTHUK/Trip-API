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

    private String requestId;

    public TripDto(TripStatus tripStatus, String accommodationId, String destinationId, String requestId) {
        this.tripStatus = tripStatus;
        this.accommodationId = accommodationId;
        this.destinationId = destinationId;
        this.requestId = requestId;
    }
}

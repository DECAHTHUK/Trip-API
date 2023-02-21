package ru.tinkoff.lab.tripAPI.business.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class DestinationDto {
    private String id;

    private String description;

    private String officeId;

    private String seatPlace;

    public DestinationDto(String description, String seatPlace) {
        this.description = description;
        this.seatPlace = seatPlace;
    }

    public DestinationDto(String description, String officeId, String seatPlace) {
        this.description = description;
        this.officeId = officeId;
        this.seatPlace = seatPlace;
    }
}

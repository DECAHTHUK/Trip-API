package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Destination {
    private String id;

    private String description;

    private Office office;

    private String seatPlace;

    public Destination(String description, Office office, String seatPlace) {
        this.description = description;
        this.office = office;
        this.seatPlace = seatPlace;
    }
}

package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;

@Data
public class Destination {
    private String id;

    private String description;

    private Office office;

    private String seatPlace;
}

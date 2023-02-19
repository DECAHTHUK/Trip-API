package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;

@Data
public class Office {
    private String id;

    private String address;

    private String description;

    public Office(String address, String description) {
        this.address = address;
        this.description = description;
    }
}

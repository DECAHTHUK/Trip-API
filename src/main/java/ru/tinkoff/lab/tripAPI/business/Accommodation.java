package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Accommodation {
    private String id;

    private String address;

    private Timestamp checkin_time;

    private Timestamp checkout_time;

    private String booking_tickets;
}

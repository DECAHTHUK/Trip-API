package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Accommodation {
    private String id;

    private String address;

    private LocalDateTime checkin_time;

    private LocalDateTime checkout_time;

    private String booking_tickets;
}

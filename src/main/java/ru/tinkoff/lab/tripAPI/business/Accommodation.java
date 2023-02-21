package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Accommodation {
    private String id;

    private String address;

    private Timestamp checkinTime;

    private Timestamp checkoutTime;

    private String bookingUrl;

    public Accommodation(String address, Timestamp checkinTime, Timestamp checkoutTime, String bookingUrl) {
        this.address = address;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.bookingUrl = bookingUrl;
    }
}

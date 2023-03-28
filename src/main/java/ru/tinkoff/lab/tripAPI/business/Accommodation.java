package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Accommodation {
    private String id;

    private String address;

    private String bookingUrl;

    public Accommodation(String address, String bookingUrl) {
        this.address = address;
        this.bookingUrl = bookingUrl;
    }
}

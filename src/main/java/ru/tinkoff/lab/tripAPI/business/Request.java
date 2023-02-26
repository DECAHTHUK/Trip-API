package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Request {
    private String id;

    private Trip trip;

    private RequestStatus requestStatus;

    private String description;

    private User worker;

    private Office office;

    private String comment;

    private Timestamp startDate;

    private Timestamp endDate;

    private String transportTo;

    private String transportFrom;

    private String ticketsUrl;
}

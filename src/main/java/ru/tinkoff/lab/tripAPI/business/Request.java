package ru.tinkoff.lab.tripAPI.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Request {
    private String id;

    private RequestStatus requestStatus;

    private String description;

    private String workerFirstName;

    private String workerSecondName;

    private String workerEmail;

    private Destination destination;

    private String comment;

    private Timestamp startDate;

    private Timestamp endDate;

    private String ticketsUrl;
}

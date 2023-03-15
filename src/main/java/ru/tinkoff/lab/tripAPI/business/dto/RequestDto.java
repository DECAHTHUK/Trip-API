package ru.tinkoff.lab.tripAPI.business.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class RequestDto {
    private String id;

    private RequestStatus requestStatus;

    private String description;

    private String workerId;

    private String destinationId;

    private String comment;

    private Timestamp startDate;

    private Timestamp endDate;

    private String ticketsUrl;

    public RequestDto(RequestStatus requestStatus, String description, String comment,
                      Timestamp startDate, Timestamp endDate, String ticketsUrl) {
        this.requestStatus = requestStatus;
        this.description = description;
        this.comment = comment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketsUrl = ticketsUrl;
    }
}

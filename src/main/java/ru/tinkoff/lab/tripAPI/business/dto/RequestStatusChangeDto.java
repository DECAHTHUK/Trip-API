package ru.tinkoff.lab.tripAPI.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusChangeDto {
    private String approverId;

    private TripDto tripDto;

    private String comment;
}

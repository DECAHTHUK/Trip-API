package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestStatusChangeDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
import ru.tinkoff.lab.tripAPI.business.enums.TripStatus;
import ru.tinkoff.lab.tripAPI.exceptions.*;
import ru.tinkoff.lab.tripAPI.mapping.RequestMapper;
import ru.tinkoff.lab.tripAPI.mapping.UserRelationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
public class RequestService {
    private final RequestMapper requestMapper;

    private final NotificationService notificationService;

    private final UserRelationMapper userRelationMapper;

    private final AccommodationDestinationTripService accommodationDestinationTripService;

    @Value("${pagination}")
    private int ROWS_AMOUNT;

    @Transactional
    public Id createRequest(RequestDto requestDto) {
        Id requestId;
        try {
            requestId = requestMapper.insertRequest(requestDto);
            List<NotificationDto> notificationDtoList = new ArrayList<>();
            for (String id : userRelationMapper.selectApproversIds(UUID.fromString(requestDto.getWorkerId()))) {
                notificationDtoList.add(new NotificationDto(requestId.getId(), false, id));
            }
            if (!notificationDtoList.isEmpty()) notificationService.createMultipleNotifications(notificationDtoList);
            return requestId;
        } catch (RequestCreateException e) {
            throw new RequestCreateException(e.getMessage());
        }
    }

    public Request getRequest(UUID uuid) {
        Request request = requestMapper.selectRequest(uuid);
        if (request == null) {
            throw new RequestNotFoundException("Request with id = " + uuid + " was not found");
        }
        return request;
    }

    public List<Request> getIncomingRequests(UUID approverId, int page) {
        return requestMapper.selectIncomingRequests(
                approverId, page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    public List<Request> getOutgoingRequests(UUID workerId, int page) {
        return requestMapper.selectOutgoingRequests(
                workerId, page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    @Transactional
    public void updateRequest(RequestDto requestDto) {
        try {
            requestMapper.updateRequest(requestDto);

            String approverId = getRequest(UUID.fromString(requestDto.getId())).getApproverId();
            if (approverId != null && !approverId.isEmpty()) {
                notificationService.createNotification(new NotificationDto(requestDto.getId(), false, approverId));
            }
        } catch (NotificationCreateException e) {
            throw new NotificationCreateException(e.getMessage());
        }
    }

    public void deleteRequest(UUID id) {
        requestMapper.deleteRequest(id);
    }

    public void changeStatus(UUID requestId, RequestStatus status, RequestStatusChangeDto requestStatusChangeDto) {
        requestMapper.updateRequestStatus(requestId, status, requestStatusChangeDto.getComment(),
                UUID.fromString(requestStatusChangeDto.getApproverId()));
    }

    @Transactional
    public Id approveRequest(UUID uuid, RequestStatusChangeDto requestStatusChangeDto) {
        requestStatusChangeDto.getTripDto().setTripStatus(TripStatus.PENDING);
        if (requestStatusChangeDto.getTripDto() == null) {
            throw new TripDtoNullException("RequestStatusChangeDto MUST have valid TripDto in body when approving request");
        }
        try {
            requestMapper.updateRequestStatus(uuid, RequestStatus.APPROVED,
                    requestStatusChangeDto.getComment(), UUID.fromString(requestStatusChangeDto.getApproverId()));
            return accommodationDestinationTripService.createTrip(requestStatusChangeDto.getTripDto());
        } catch (TripCreateException e) {
            throw new TripCreateException(e.getMessage());
        }
    }
}

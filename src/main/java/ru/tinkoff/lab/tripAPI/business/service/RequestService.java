package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
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

    private final PlatformTransactionManager transactionManager;

    @Value("${pagination}")
    private int ROWS_AMOUNT;

    public Id createRequest(RequestDto requestDto) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        Id requestId = new Id();
        try {
            requestId = requestMapper.insertRequest(requestDto);
            List<NotificationDto> notificationDtoList = new ArrayList<>();
            for (String id : userRelationMapper.selectApproversIds(UUID.fromString(requestDto.getWorkerId()))) {
                notificationDtoList.add(new NotificationDto(requestId.getId(), false, id));
            }
            notificationService.createMultipleNotifications(notificationDtoList);
            transactionManager.commit(transactionStatus);
            return requestId;
        } catch (RuntimeException e) {
            transactionManager.rollback(transactionStatus);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Request getRequest(UUID uuid) {
        Request request = requestMapper.selectRequest(uuid);
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request with id = " + uuid + " was not found");
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

    public void updateRequest(RequestDto requestDto) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            requestMapper.updateRequest(requestDto);

            String approverId = getRequest(UUID.fromString(requestDto.getId())).getApproverId();
            if (approverId == null || approverId.isEmpty()) {
                List<NotificationDto> notificationDtoList = new ArrayList<>();
                for (String id : userRelationMapper.selectApproversIds(UUID.fromString(requestDto.getWorkerId()))) {
                    notificationDtoList.add(new NotificationDto(requestDto.getId(), false, id));
                }
                notificationService.createMultipleNotifications(notificationDtoList);
            } else {
                notificationService.createNotification(new NotificationDto(requestDto.getId(), false, approverId));
            }

            transactionManager.commit(transactionStatus);
        } catch (RuntimeException e) {
            transactionManager.rollback(transactionStatus);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void deleteRequest(UUID id) {
        requestMapper.deleteRequest(id);
    }

    public void changeStatus(UUID requestId, RequestStatus status, String comment, UUID approverId) {
        requestMapper.updateRequestStatus(requestId, status, comment, approverId);
    }

    public Id approveRequest(UUID uuid, TripDto tripDto, String comment, UUID approverId) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            requestMapper.updateRequestStatus(uuid, RequestStatus.APPROVED, comment, approverId);
            Id tripId = accommodationDestinationTripService.createTrip(tripDto);
            transactionManager.commit(transactionStatus);
            return tripId;
        } catch (RuntimeException e) {
            transactionManager.rollback(transactionStatus);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

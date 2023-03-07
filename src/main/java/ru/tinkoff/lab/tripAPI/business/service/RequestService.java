package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.mapping.RequestMapper;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
public class RequestService {
    private final RequestMapper requestMapper;

    @Value("${pagination}")
    private int ROWS_AMOUNT;

    public Id createRequest(RequestDto requestDto) {
        try {
            return requestMapper.insertRequest(requestDto);
        } catch (RuntimeException e) {
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

    public List<Request> getIncomingRequests(UUID bossId, int page) {
        return requestMapper.selectIncomingRequests(
                bossId, page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    //TODO do we have to change type of workerId to uuid?
    public List<Request> getOutgoingRequests(UUID workerId, int page) {
        return requestMapper.selectOutgoingRequests(
                workerId, page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    public void updateRequest(RequestDto requestDto) {
        requestMapper.updateRequest(requestDto);
    }

    public void deleteRequest(UUID id) {
        requestMapper.deleteRequest(id);
    }
}

package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.mapping.RequestMapper;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestMapper requestMapper;

    private static final int ROWS_AMOUNT = 5;

    private static final Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public int getRowsAmount() {
        return ROWS_AMOUNT;
    }

    public Id createRequest(RequestDto requestDto) {
        try {
            return requestMapper.insertRequest(requestDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Request getRequest(String uuid) {
        Request request = requestMapper.selectRequest(UUID.fromString(uuid));
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request with id = " + uuid + " was not found");
        }
        return request;
    }

    public List<Request> getIncomingRequests(String bossId, int page) {
        return requestMapper.selectIncomingRequests(
                UUID.fromString(bossId), page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    //TODO ask if we need to check for valid uuid as down below
    public List<Request> getOutgoingRequests(String workerId, int page) {
        if (!UUID_REGEX.matcher(workerId).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect UUID format for string + " + workerId);
        }
        return requestMapper.selectOutgoingRequests(
                UUID.fromString(workerId), page * ROWS_AMOUNT - ROWS_AMOUNT, ROWS_AMOUNT);
    }

    public void updateRequest(RequestDto requestDto) {
        requestMapper.updateRequest(requestDto);
    }

    public void deleteRequest(String id) {
        requestMapper.deleteRequest(UUID.fromString(id));
    }
}

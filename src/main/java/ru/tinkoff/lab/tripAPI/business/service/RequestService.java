package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.mapping.RequestMapper;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestMapper requestMapper;


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
}

package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.service.RequestService;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping("")
    public Id createNewRequest(@RequestBody RequestDto requestDto) {
        return requestService.createRequest(requestDto);
    }

    @GetMapping("/{uuid}")
    public Request getRequestById(@PathVariable String uuid) {
        return requestService.getRequest(uuid);
    }

    @PutMapping("")
    public void updateRequest(@RequestBody RequestDto requestDto) {
        requestService.updateRequest(requestDto);
    }

    @DeleteMapping("/{uuid}")
    public void deleteRequest(@PathVariable String uuid) {
        requestService.deleteRequest(uuid);
    }
}

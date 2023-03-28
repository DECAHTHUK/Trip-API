package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;
import ru.tinkoff.lab.tripAPI.business.dto.RequestStatusChangeDto;
import ru.tinkoff.lab.tripAPI.business.enums.RequestStatus;
import ru.tinkoff.lab.tripAPI.business.service.RequestService;

import java.util.UUID;

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
    public Request getRequestById(@PathVariable UUID uuid) {
        return requestService.getRequest(uuid);
    }

    @PutMapping("")
    public void updateRequest(@RequestBody RequestDto requestDto) {
        requestService.updateRequest(requestDto);
    }

    @DeleteMapping("/{uuid}")
    public void deleteRequest(@PathVariable UUID uuid) {
        requestService.deleteRequest(uuid);
    }

    @PutMapping("/{uuid}/approve")
    public Id approveRequest(@PathVariable UUID uuid, @RequestBody RequestStatusChangeDto requestStatusChangeDto) {
        return requestService.approveRequest(uuid, requestStatusChangeDto);
    }

    @PutMapping("/{uuid}/send-for-editing")
    public void sendRequestForEditing(@PathVariable UUID uuid, @RequestBody RequestStatusChangeDto requestStatusChangeDto) {
        requestService.changeStatus(uuid, RequestStatus.AWAIT_CHANGES, requestStatusChangeDto);
    }

    @PutMapping("/{uuid}/decline")
    public void declineRequest(@PathVariable UUID uuid, @RequestBody RequestStatusChangeDto requestStatusChangeDto) {
        requestService.changeStatus(uuid, RequestStatus.DECLINED, requestStatusChangeDto);
    }
}

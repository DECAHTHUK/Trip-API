package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Destination;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.service.DestinationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping("")
    public Id createNewDestination(@RequestBody DestinationDto destinationDto) {
        return destinationService.createDestination(destinationDto);
    }

    @GetMapping("/{uuid}")
    public Destination getDestination(@PathVariable String uuid) {
        return destinationService.getDestination(uuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteDestination(@PathVariable String uuid) {
        destinationService.deleteDestination(uuid);
    }

    @PutMapping("")
    public void updateDestination(@RequestBody DestinationDto destinationDto) {
        destinationService.updateDestination(destinationDto);
    }
}
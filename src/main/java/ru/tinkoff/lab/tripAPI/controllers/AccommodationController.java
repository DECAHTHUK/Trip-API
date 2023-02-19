package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Accommodation;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.service.AccommodationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @PostMapping(value = "")
    public Id createNewAccommodation(@RequestBody Accommodation accommodation) {
        return accommodationService.createAccommodation(accommodation);
    }

    @DeleteMapping(value = "/{uuid}")
    public void deleteAccommodation(@PathVariable String uuid) {
        accommodationService.deleteAccommodation(uuid);
    }

    @GetMapping(value = "/{uuid}")
    public Accommodation getAccommodation(@PathVariable String uuid) {
        return accommodationService.getAccommodation(uuid);
    }

    @PutMapping(value = "")
    public void updateAccommodation(Accommodation accommodation) {
        accommodationService.updateAccommodation(accommodation);
    }
}
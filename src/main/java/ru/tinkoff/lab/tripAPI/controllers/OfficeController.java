package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.business.service.OfficeService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
@PreAuthorize("hasAuthority('ADMIN')")
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping(path = "")
    public Id createNewOffice(@RequestBody Office office) {
        return officeService.createOffice(office);
    }

    @GetMapping("/{uuid}")
    public Office getOfficeById(@PathVariable UUID uuid) {
        return officeService.getOffice(uuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteOfficeById(@PathVariable UUID uuid) {
        officeService.deleteOffice(uuid);
    }

    @PutMapping("")
    public void updateOffice(@RequestBody Office office) {
        officeService.updateOffice(office);
    }
}

package ru.tinkoff.lab.tripAPI.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.business.service.OfficeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping("")
    public Id createNewOffice(@RequestBody Office office) {
        return officeService.createOffice(office);
    }

    @GetMapping("/{uuid}")
    public Office getOfficeById(@PathVariable String uuid) {
        return officeService.getOffice(uuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteOfficeById(@PathVariable String uuid) {
        officeService.deleteOffice(uuid);
    }

    @PutMapping("")
    public void updateOffice(@RequestBody Office office) {
        officeService.updateOffice(office);
    }
}
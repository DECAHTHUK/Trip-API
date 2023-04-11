package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.exceptions.OfficeCreateException;
import ru.tinkoff.lab.tripAPI.exceptions.OfficeNotFoundException;
import ru.tinkoff.lab.tripAPI.mapping.OfficeMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeMapper officeMapper;

    public Id createOffice(Office office) {
        try {
            return officeMapper.insertOffice(office);
        } catch (Exception e) {
            throw new OfficeCreateException(e.getMessage());
        }
    }

    public Office getOffice(UUID uuid) {
        Office office = officeMapper.selectOffice(uuid);
        if (office == null) {
            throw new OfficeNotFoundException("Office with id = " + uuid + " was not found");
        }
        return office;
    }

    public void deleteOffice(UUID uuid) {
        officeMapper.deleteOffice(uuid);
    }

    public void updateOffice(Office office) {
        officeMapper.updateOffice(office);
    }
}
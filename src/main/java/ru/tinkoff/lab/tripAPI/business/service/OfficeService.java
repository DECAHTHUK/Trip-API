package ru.tinkoff.lab.tripAPI.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;
import ru.tinkoff.lab.tripAPI.mapping.OfficeMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeMapper officeMapper;

    public Id createOffice(Office office) {
        try {
            return officeMapper.insertOffice(office);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public Office getOffice(String uuid) {
        return officeMapper.selectOffice(UUID.fromString(uuid));
    }

    public void deleteOffice(String uuid) {
        officeMapper.deleteOffice(UUID.fromString(uuid));
    }

    public void updateOffice(Office office) {
        officeMapper.updateOffice(office);
    }
}
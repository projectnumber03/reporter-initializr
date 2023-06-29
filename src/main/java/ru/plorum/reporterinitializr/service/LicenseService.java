package ru.plorum.reporterinitializr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.plorum.reporterinitializr.model.License;
import ru.plorum.reporterinitializr.model.User;
import ru.plorum.reporterinitializr.repository.LicenseRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LicenseService {

    private final LicenseRepository licenseRepository;

    public Optional<License> findByUser(final User user) {
        if (Objects.isNull(user)) return Optional.empty();
        return licenseRepository.findByUser(user);
    }

    public void save(final License license) {
        if (Objects.isNull(license)) return;
        licenseRepository.save(license);
    }

}

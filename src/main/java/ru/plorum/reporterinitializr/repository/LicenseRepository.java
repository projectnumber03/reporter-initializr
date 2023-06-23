package ru.plorum.reporterinitializr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plorum.reporterinitializr.model.License;

import java.util.UUID;

public interface LicenseRepository extends JpaRepository<License, UUID> {
}

package ru.plorum.reporterinitializr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plorum.reporterinitializr.model.Requisite;

import java.util.UUID;

public interface RequisiteRepository extends JpaRepository<Requisite, UUID> {
}

package ru.plorum.reporterinitializr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plorum.reporterinitializr.model.Director;

import java.util.UUID;

public interface DirectorRepository extends JpaRepository<Director, UUID> {
}

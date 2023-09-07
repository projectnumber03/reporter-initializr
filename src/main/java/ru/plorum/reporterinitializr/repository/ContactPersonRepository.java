package ru.plorum.reporterinitializr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plorum.reporterinitializr.model.ContactPerson;

import java.util.UUID;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, UUID> {
}

package ru.plorum.reporterinitializr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plorum.reporterinitializr.model.Client;
import ru.plorum.reporterinitializr.model.User;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {
    List<Client> findAllByUser(final User user);
}

package ru.plorum.reporterinitializr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.plorum.reporterinitializr.model.Client;
import ru.plorum.reporterinitializr.model.User;
import ru.plorum.reporterinitializr.repository.ClientRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> findAllByUser(final User user) {
        return Optional.ofNullable(user)
                .map(clientRepository::findAllByUser)
                .orElse(Collections.emptyList());
    }

    public Optional<Client> findById(final String id) {
        return Optional.ofNullable(id).flatMap(clientRepository::findById);
    }

    public void delete(final Client client) {
        Optional.ofNullable(client).ifPresent(clientRepository::delete);
    }

}

package ru.plorum.reporterinitializr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.plorum.reporterinitializr.component.profile.FieldsCreatable;
import ru.plorum.reporterinitializr.component.profile.PhysicalProfileFields;
import ru.plorum.reporterinitializr.model.Client;
import ru.plorum.reporterinitializr.repository.ClientRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PhysicalClientService implements IClientService {

    private final ClientRepository clientRepository;

    private final PhoneNumberService phoneNumberService;

    private final UserService userService;

    @Override
    public boolean check(final Client.Type type) {
        return type == Client.Type.PHYSICAL;
    }

    @Override
    public void save(final FieldsCreatable physicalFields) {
        if (!(physicalFields instanceof PhysicalProfileFields fields)) return;
        final var client = fields.toClient();
        Optional.ofNullable(client.getPhone())
                .map(phoneNumberService::format)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(client::setPhone);
        client.setUser(userService.getAuthenticatedUser());
        clientRepository.save(client);
    }

}

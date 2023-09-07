package ru.plorum.reporterinitializr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.plorum.reporterinitializr.component.profile.FieldsCreatable;
import ru.plorum.reporterinitializr.component.profile.JuridicalProfileFields;
import ru.plorum.reporterinitializr.model.Client;
import ru.plorum.reporterinitializr.repository.ClientRepository;
import ru.plorum.reporterinitializr.repository.ContactPersonRepository;
import ru.plorum.reporterinitializr.repository.DirectorRepository;
import ru.plorum.reporterinitializr.repository.RequisiteRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JuridicalClientService implements IClientService {

    private final ClientRepository clientRepository;

    private final RequisiteRepository requisiteRepository;

    private final DirectorRepository directorRepository;

    private final ContactPersonRepository contactPersonRepository;

    private final PhoneNumberService phoneNumberService;

    private final UserService userService;

    @Override
    public boolean check(final Client.Type type) {
        return type == Client.Type.JURIDICAL;
    }

    @Override
    public void save(final FieldsCreatable juridicalFields) {
        if (!(juridicalFields instanceof JuridicalProfileFields fields)) return;
        final var client = fields.toClient();
        client.setUser(userService.getAuthenticatedUser());
        final var contactPerson = client.getContactPerson();
        Optional.ofNullable(contactPerson.getPhone())
                .map(phoneNumberService::format)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(contactPerson::setPhone);
        final var requisites = client.getRequisites();
        final var director = client.getDirector();
//        contactPersonRepository.save(contactPerson);
//        requisiteRepository.save(requisites);
//        directorRepository.save(director);
        clientRepository.save(client);
    }

}

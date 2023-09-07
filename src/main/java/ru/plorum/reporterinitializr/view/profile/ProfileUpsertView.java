package ru.plorum.reporterinitializr.view.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import ru.plorum.reporterinitializr.component.ErrorNotification;
import ru.plorum.reporterinitializr.component.profile.JuridicalProfileFields;
import ru.plorum.reporterinitializr.component.profile.PhysicalProfileFields;
import ru.plorum.reporterinitializr.model.Client;
import ru.plorum.reporterinitializr.service.ClientService;
import ru.plorum.reporterinitializr.service.IClientService;
import ru.plorum.reporterinitializr.view.AbstractView;
import ru.plorum.reporterinitializr.view.MainView;
import ru.plorum.reporterinitializr.view.Validatable;

import java.util.Collections;
import java.util.List;

import static ru.plorum.reporterinitializr.util.Constants.*;

@PermitAll
@PageTitle(PROFILE)
@RequiredArgsConstructor
@Route(value = "profile/upsert", layout = MainView.class)
public class ProfileUpsertView extends AbstractView implements HasUrlParameter<String>, Validatable {

    private final ComboBox<Client.Type> clientTypeField = new ComboBox<>(TYPE);

    private final PhysicalProfileFields physicalProfileFields;

    private final JuridicalProfileFields juridicalProfileFields;

    private final List<IClientService> clientServices;

    private final ClientService clientService;

    private final Button saveButton = new Button(SAVE);

    private Registration saveListener;

    @Override
    @PostConstruct
    protected void initialize() {
        super.initialize();
        vertical.add(createClientTypeField());
        vertical.add(physicalProfileFields.createFields());
        vertical.add(juridicalProfileFields.createFields());
        vertical.add(createSaveButton());
        add(vertical);
    }

    private Button createSaveButton() {
        saveListener = saveButton.addClickListener(e -> {
            if (!validate()) return;
            clientServices.forEach(service -> {
                if (service.check(clientTypeField.getValue())) {
                    service.save(physicalProfileFields);
                    service.save(juridicalProfileFields);
                }
            });
            saveButton.getUI().ifPresent(ui -> ui.navigate("profile"));
        });
        return saveButton;
    }

    private Component createClientTypeField() {
        clientTypeField.getElement().setAttribute("theme", "small");
        clientTypeField.setItemLabelGenerator(Client.Type::getName);
        clientTypeField.setItems(Client.Type.values());
        clientTypeField.setValue(Client.Type.PHYSICAL);
        physicalProfileFields.setVisible(true);
        juridicalProfileFields.setVisible(false);
        clientTypeField.setAllowCustomValue(false);
        clientTypeField.addValueChangeListener(event -> {
            physicalProfileFields.setVisible(event.getValue() == Client.Type.PHYSICAL);
            juridicalProfileFields.setVisible(event.getValue() == Client.Type.JURIDICAL);
        });
        return clientTypeField;
    }

    @Override
    public void setParameter(final BeforeEvent event, @OptionalParameter final String s) {
        final var location = event.getLocation();
        final var queryParameters = location.getQueryParameters();
        final var parametersMap = queryParameters.getParameters();
        final var id = parametersMap.getOrDefault("id", Collections.emptyList());
        if (CollectionUtils.isEmpty(id)) return;
        final var client = clientService.findById(id.iterator().next());
        if (client.isEmpty()) return;
        final var clientType = client.get().getType();
        clientTypeField.setValue(clientType);
        physicalProfileFields.setVisible(clientType == Client.Type.PHYSICAL);
        juridicalProfileFields.setVisible(clientType == Client.Type.JURIDICAL);
        physicalProfileFields.setFields(client.get());
        juridicalProfileFields.setFields(client.get());
        saveListener.remove();
        saveListener = saveButton.addClickListener(e -> {
            if (!validate()) return;
            clientService.delete(client.get());
            clientServices.forEach(service -> {
                if (service.check(clientTypeField.getValue())) {
                    service.save(physicalProfileFields);
                    service.save(juridicalProfileFields);
                }
            });
            saveButton.getUI().ifPresent(ui -> ui.navigate("profile"));
        });
    }

    @Override
    public boolean validate() {
        final Binder<Client> clientBinder = new BeanValidationBinder<>(Client.class);
        if (clientTypeField.getValue() == Client.Type.PHYSICAL) {
            clientBinder.forField(physicalProfileFields.getFirstNameField()).asRequired(REQUIRED_FIELD).bind(Client::getFirstName, Client::setFirstName);
            clientBinder.forField(physicalProfileFields.getSecondNameField()).asRequired(REQUIRED_FIELD).bind(Client::getSecondName, Client::setSecondName);
            clientBinder.forField(physicalProfileFields.getEmailField()).asRequired(REQUIRED_FIELD).bind(Client::getEmail, Client::setEmail);
            clientBinder.forField(physicalProfileFields.getPhoneField()).asRequired(REQUIRED_FIELD).bind(Client::getPhone, Client::setPhone);
            if (!physicalProfileFields.getPersonalDataAgreement().getValue()) {
                new ErrorNotification("Необходимо согласие на обработку персональных данных");
                return false;
            }
        }
        if (clientTypeField.getValue() == Client.Type.JURIDICAL) {
            final var requisiteFields = juridicalProfileFields.getRequisiteFields();
            clientBinder.forField(requisiteFields.getNameField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getName(), (c, name) -> c.getRequisites().setName(name));
            clientBinder.forField(requisiteFields.getInnField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getInn(), (c, inn) -> c.getRequisites().setInn(inn));
            clientBinder.forField(requisiteFields.getKppField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getKpp(), (c, kpp) -> c.getRequisites().setKpp(kpp));
            clientBinder.forField(requisiteFields.getOgrnField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getOgrn(), (c, ogrn) -> c.getRequisites().setOgrn(ogrn));
            clientBinder.forField(requisiteFields.getOkpoField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getOkpo(), (c, okpo) -> c.getRequisites().setOkpo(okpo));
            clientBinder.forField(requisiteFields.getOktmoField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getOktmo(), (c, oktmo) -> c.getRequisites().setOktmo(oktmo));
            clientBinder.forField(requisiteFields.getAddressField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getAddress(), (c, address) -> c.getRequisites().setAddress(address));
            clientBinder.forField(requisiteFields.getBankField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getBank(), (c, bank) -> c.getRequisites().setBank(bank));
            clientBinder.forField(requisiteFields.getBikField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getBik(), (c, bik) -> c.getRequisites().setBik(bik));
            clientBinder.forField(requisiteFields.getCorrespondentAccountField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getCorrespondentAccount(), (c, correspondentAccount) -> c.getRequisites().setCorrespondentAccount(correspondentAccount));
            clientBinder.forField(requisiteFields.getBankAccountField()).asRequired(REQUIRED_FIELD).bind(c -> c.getRequisites().getBankAccount(), (c, bankAccount) -> c.getRequisites().setBankAccount(bankAccount));
            final var directorFields = juridicalProfileFields.getDirectorFields();
            clientBinder.forField(directorFields.getFirstNameField()).asRequired(REQUIRED_FIELD).bind(c -> c.getDirector().getFirstName(), (c, firstName) -> c.getDirector().setFirstName(firstName));
            clientBinder.forField(directorFields.getSecondNameField()).asRequired(REQUIRED_FIELD).bind(c -> c.getDirector().getSecondName(), (c, secondName) -> c.getDirector().setSecondName(secondName));
            final var contactPersonFields = juridicalProfileFields.getContactPersonFields();
            clientBinder.forField(contactPersonFields.getFirstNameField()).asRequired(REQUIRED_FIELD).bind(c -> c.getContactPerson().getFirstName(), (c, firstName) -> c.getContactPerson().setFirstName(firstName));
            clientBinder.forField(contactPersonFields.getSecondNameField()).asRequired(REQUIRED_FIELD).bind(c -> c.getContactPerson().getSecondName(), (c, secondName) -> c.getContactPerson().setSecondName(secondName));
            clientBinder.forField(contactPersonFields.getEmailField()).asRequired(REQUIRED_FIELD).bind(c -> c.getContactPerson().getEmail(), (c, email) -> c.getContactPerson().setEmail(email));
            clientBinder.forField(contactPersonFields.getPhoneField()).asRequired(REQUIRED_FIELD).bind(c -> c.getContactPerson().getPhone(), (c, phone) -> c.getContactPerson().setEmail(phone));
        }

        return clientBinder.validate().isOk();
    }

}

package ru.plorum.reporterinitializr.component.profile;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.plorum.reporterinitializr.model.Client;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Getter
@RouteScope
@SpringComponent
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JuridicalProfileFields implements VisibilityChangeable, FieldsCreatable {

    RequisiteFields requisiteFields;

    DirectorFields directorFields;

    ContactPersonFields contactPersonFields;

    VerticalLayout layout = new VerticalLayout();

    @Override
    public void setVisible(boolean value) {
        requisiteFields.setVisible(value);
        directorFields.setVisible(value);
        contactPersonFields.setVisible(value);
    }

    @Override
    public VerticalLayout createFields() {
        layout.removeAll();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(
                requisiteFields.createFields()
                , directorFields.createFields()
                , contactPersonFields.createFields()
        );

        return layout;
    }

    public void setFields(final Client client) {
        if (Objects.isNull(client) || client.getType() == Client.Type.PHYSICAL) return;
        Optional.ofNullable(client.getRequisites()).ifPresent(requisiteFields::setFields);
        Optional.ofNullable(client.getDirector()).ifPresent(directorFields::setFields);
        Optional.ofNullable(client.getContactPerson()).ifPresent(contactPersonFields::setFields);
    }

    public Client toClient() {
        final var client = new Client();
        client.setType(Client.Type.JURIDICAL);
        client.setCreatedOn(Optional.ofNullable(client.getCreatedOn()).orElse(LocalDateTime.now()));
        client.setRequisites(requisiteFields.toRequisite());
        client.setDirector(directorFields.toDirector());
        client.setContactPerson(contactPersonFields.toContactPerson());

        return client;
    }

}

package ru.plorum.reporterinitializr.component.profile;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.vaadin.stefan.table.Table;
import ru.plorum.reporterinitializr.model.ContactPerson;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static ru.plorum.reporterinitializr.util.Constants.EMAIL;

@Getter
@RouteScope
@SpringComponent
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactPersonFields implements VisibilityChangeable, FieldsCreatable {

    TextField secondNameField = new TextField();

    TextField firstNameField = new TextField();

    TextField lastNameField = new TextField();

    TextField emailField = new TextField();

    TextField phoneField = new TextField();

    VerticalLayout layout = new VerticalLayout();

    @Override
    public void setVisible(final boolean value) {
        layout.setVisible(value);
    }

    @Override
    public VerticalLayout createFields() {
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.removeAll();
        layout.add(new H5("Данные контактного лица"));
        final var table = new Table();
        final Consumer<TextFieldDTO> createFieldConsumer = dto -> {
            final var row = table.addRow();
            row.addDataCell().add(createLabel(dto.text()));
            final var cell = row.addDataCell();
            dto.field().addThemeVariants(TextFieldVariant.LUMO_SMALL);
            dto.field().setWidth(100, Unit.PERCENTAGE);
            cell.add(dto.field());
        };
        createFieldConsumer.accept(new TextFieldDTO(secondNameField, "Фамилия"));
        createFieldConsumer.accept(new TextFieldDTO(firstNameField, "Имя"));
        createFieldConsumer.accept(new TextFieldDTO(lastNameField, "Отчество"));
        createFieldConsumer.accept(new TextFieldDTO(emailField, EMAIL));
        createFieldConsumer.accept(new TextFieldDTO(phoneField, "Телефон"));
        layout.add(table);

        return layout;
    }

    public ContactPerson toContactPerson() {
        final var contactPerson = new ContactPerson();
        contactPerson.setId(UUID.randomUUID());
        contactPerson.setSecondName(secondNameField.getValue());
        contactPerson.setFirstName(firstNameField.getValue());
        contactPerson.setLastName(lastNameField.getValue());
        contactPerson.setEmail(emailField.getValue());
        contactPerson.setPhone(phoneField.getValue());

        return contactPerson;
    }

    public void setFields(final ContactPerson contactPerson) {
        secondNameField.setValue(contactPerson.getSecondName());
        firstNameField.setValue(contactPerson.getFirstName());
        Optional.ofNullable(contactPerson.getLastName()).ifPresent(lastNameField::setValue);
        emailField.setValue(contactPerson.getEmail());
        phoneField.setValue(contactPerson.getPhone());
    }

}

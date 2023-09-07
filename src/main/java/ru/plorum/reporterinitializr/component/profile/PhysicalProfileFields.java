package ru.plorum.reporterinitializr.component.profile;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.vaadin.stefan.table.Table;
import ru.plorum.reporterinitializr.model.Client;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static ru.plorum.reporterinitializr.util.Constants.EMAIL;

@Getter
@RouteScope
@SpringComponent
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhysicalProfileFields implements VisibilityChangeable, FieldsCreatable {

    TextField secondNameField = new TextField();

    TextField firstNameField = new TextField();

    TextField lastNameField = new TextField();

    TextField emailField = new TextField();

    TextField phoneField = new TextField();

    Checkbox personalDataAgreement = new Checkbox("Согласие на обработку персональных данных");

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
        personalDataAgreement.getStyle().set("font-size", "85%");
        layout.add(personalDataAgreement);

        return layout;
    }

    public void setFields(final Client client) {
        if (Objects.isNull(client) || client.getType() == Client.Type.JURIDICAL) return;
        secondNameField.setValue(client.getSecondName());
        firstNameField.setValue(client.getFirstName());
        Optional.ofNullable(client.getLastName()).ifPresent(lastNameField::setValue);
        emailField.setValue(client.getEmail());
        phoneField.setValue(client.getPhone());
        personalDataAgreement.setValue(true);
    }

    public Client toClient() {
        final Client client = new Client();
        client.setType(Client.Type.PHYSICAL);
        client.setFirstName(firstNameField.getValue());
        client.setSecondName(secondNameField.getValue());
        client.setLastName(lastNameField.getValue());
        client.setEmail(emailField.getValue());
        client.setPhone(phoneField.getValue());
        client.setCreatedOn(Optional.ofNullable(client.getCreatedOn()).orElse(LocalDateTime.now()));

        return client;
    }

}

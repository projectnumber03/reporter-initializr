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
import ru.plorum.reporterinitializr.model.Director;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@RouteScope
@SpringComponent
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectorFields implements VisibilityChangeable, FieldsCreatable {

    TextField secondNameField = new TextField();

    TextField firstNameField = new TextField();

    TextField lastNameField = new TextField();

    VerticalLayout layout = new VerticalLayout();

    @Override
    public void setVisible(boolean value) {
        layout.setVisible(value);
    }

    @Override
    public VerticalLayout createFields() {
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.removeAll();
        layout.add(new H5("Данные подписанта договора (Генеральный директор или доверенное лицо)"));
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
        layout.add(table);

        return layout;
    }

    public Director toDirector() {
        final var director = new Director();
        director.setId(UUID.randomUUID());
        director.setSecondName(secondNameField.getValue());
        director.setFirstName(firstNameField.getValue());
        director.setLastName(lastNameField.getValue());

        return director;
    }

    public void setFields(final Director director) {
        secondNameField.setValue(director.getSecondName());
        firstNameField.setValue(director.getFirstName());
        Optional.ofNullable(director.getLastName()).ifPresent(lastNameField::setValue);
    }

}

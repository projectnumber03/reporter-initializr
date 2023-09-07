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
import ru.plorum.reporterinitializr.model.Requisite;

import java.util.UUID;

@Getter
@RouteScope
@SpringComponent
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequisiteFields implements VisibilityChangeable, FieldsCreatable {

    TextField nameField = new TextField();

    TextField innField = new TextField();

    TextField kppField = new TextField();

    TextField ogrnField = new TextField();

    TextField okpoField = new TextField();

    TextField oktmoField = new TextField();

    TextField addressField = new TextField();

    TextField bankField = new TextField();

    TextField bikField = new TextField();

    TextField correspondentAccountField = new TextField();

    TextField bankAccountField = new TextField();

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
        final var table = new Table();
        final var nameRow = table.addRow();
        nameRow.addDataCell().add(createLabel("Название"));
        final var nameFieldCell = nameRow.addDataCell();
        nameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        nameField.setWidth(100, Unit.PERCENTAGE);
        nameFieldCell.add(nameField);
        nameFieldCell.setColSpan(6);
        final var innKppOgrnRow = table.addRow();
        innKppOgrnRow.addDataCell().add(createLabel("ИНН"));
        innField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        innField.setWidth(100, Unit.PERCENTAGE);
        kppField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        kppField.setWidth(100, Unit.PERCENTAGE);
        ogrnField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        ogrnField.setWidth(100, Unit.PERCENTAGE);
        final var innFieldCell = innKppOgrnRow.addDataCell();
        innFieldCell.setColSpan(2);
        innFieldCell.add(innField);
        final var kppFieldCell = innKppOgrnRow.addDataCell();
        kppFieldCell.setColSpan(2);
        kppFieldCell.add(createHorizontalLayout(createLabel("КПП"), kppField));
        final var ogrnFieldCell = innKppOgrnRow.addDataCell();
        ogrnFieldCell.setColSpan(2);
        ogrnFieldCell.add(createHorizontalLayout(createLabel("ОГРН"), ogrnField));
        final var okpoOktmoRow = table.addRow();
        okpoOktmoRow.addDataCell().add(createLabel("ОКПО"));
        final var okpoFieldCell = okpoOktmoRow.addDataCell();
        okpoField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        okpoField.setWidth(100, Unit.PERCENTAGE);
        okpoFieldCell.setColSpan(3);
        okpoFieldCell.add(okpoField);
        oktmoField.setWidth(100, Unit.PERCENTAGE);
        oktmoField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        final var oktmoFieldCell = okpoOktmoRow.addDataCell();
        oktmoFieldCell.setColSpan(3);
        oktmoFieldCell.add(createHorizontalLayout(createLabel("ОКТМО"), oktmoField));
        final var addressRow = table.addRow();
        addressRow.addDataCell().add(createLabel("Юр.адрес"));
        final var addressFieldCell = addressRow.addDataCell();
        addressField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        addressField.setWidth(100, Unit.PERCENTAGE);
        addressFieldCell.add(addressField);
        addressFieldCell.setColSpan(6);
        final var bankRow = table.addRow();
        bankRow.addDataCell().add(createLabel("Банк"));
        bankField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        bankField.setWidth(100, Unit.PERCENTAGE);
        bikField.setWidth(100, Unit.PERCENTAGE);
        bikField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        correspondentAccountField.setWidth(100, Unit.PERCENTAGE);
        correspondentAccountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        final var bankFieldCell = bankRow.addDataCell();
        bankFieldCell.setColSpan(2);
        bankFieldCell.add(bankField);
        final var bikFieldCell = bankRow.addDataCell();
        bikFieldCell.setColSpan(2);
        bikFieldCell.add(createHorizontalLayout(createLabel("БИК"), bikField));
        final var correspondentAccountFieldCell = bankRow.addDataCell();
        correspondentAccountFieldCell.setColSpan(2);
        correspondentAccountFieldCell.add(createHorizontalLayout(createLabel("Корр.счёт"), correspondentAccountField));
        final var bankAccountRow = table.addRow();
        bankAccountRow.addDataCell().add(createLabel("Расчётный счёт"));
        final var bankAccountFieldCell = bankAccountRow.addDataCell();
        bankAccountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        bankAccountField.setWidth(100, Unit.PERCENTAGE);
        bankAccountFieldCell.add(bankAccountField);
        bankAccountFieldCell.setColSpan(6);
        layout.add(new H5("Реквизиты организации"));
        layout.add(table);

        return layout;
    }

    public void setFields(final Requisite requisite) {
        nameField.setValue(requisite.getName());
        innField.setValue(requisite.getInn());
        kppField.setValue(requisite.getKpp());
        ogrnField.setValue(requisite.getOgrn());
        okpoField.setValue(requisite.getOkpo());
        oktmoField.setValue(requisite.getOktmo());
        addressField.setValue(requisite.getAddress());
        bankField.setValue(requisite.getBank());
        bikField.setValue(requisite.getBik());
        correspondentAccountField.setValue(requisite.getCorrespondentAccount());
        bankAccountField.setValue(requisite.getBankAccount());
    }

    public Requisite toRequisite() {
        final var requisite = new Requisite();
        requisite.setId(UUID.randomUUID());
        requisite.setName(nameField.getValue());
        requisite.setInn(innField.getValue());
        requisite.setKpp(kppField.getValue());
        requisite.setOgrn(ogrnField.getValue());
        requisite.setOkpo(okpoField.getValue());
        requisite.setOktmo(oktmoField.getValue());
        requisite.setAddress(addressField.getValue());
        requisite.setBank(bankField.getValue());
        requisite.setBik(bikField.getValue());
        requisite.setCorrespondentAccount(correspondentAccountField.getValue());
        requisite.setBankAccount(bankAccountField.getValue());

        return requisite;
    }

}

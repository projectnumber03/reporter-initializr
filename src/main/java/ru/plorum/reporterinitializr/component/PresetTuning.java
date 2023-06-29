package ru.plorum.reporterinitializr.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.plorum.reporterinitializr.component.connection.*;
import ru.plorum.reporterinitializr.model.License;
import ru.plorum.reporterinitializr.service.ZipService;

import java.util.*;

@Slf4j
@Getter
public class PresetTuning extends VerticalLayout {

    private final NumberField portNumberField = new NumberField("Порт приложения");

    private final TextField dbHostField = new TextField("Адрес БД");

    private final NumberField dbPortNumberField = new NumberField("Порт БД");

    private final TextField dbNameField = new TextField("Схема БД");

    private final TextField dbUserField = new TextField("Пользователь БД");

    private final PasswordField dbPasswordField = new PasswordField("Пароль БД");

    private final ComboBox<Connection> dbTypeField = new ComboBox<>("Тип БД");

    private final TextField adminLoginField = new TextField("Логин администратора");

    private final PasswordField adminPasswordField = new PasswordField("Пароль администратора");

    private final Button downloadButton = new Button("Скачать");

    private final ZipService zipService;

    private final License license;

    public PresetTuning(final ZipService zipService, final License license) {
        this.zipService = zipService;
        this.license = license;
        dbTypeField.setItemLabelGenerator(Connection::getConnectionType);
        final var h2Connection = new H2Connection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField, dbNameField);
        dbTypeField.setItems(
                h2Connection,
                new MSSQLConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField, dbNameField),
                new MYSQLConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField, dbNameField),
                new OracleConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField, dbNameField),
                new PostgresConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField, dbNameField)
        );
        add(portNumberField);
        if (Objects.nonNull(license) && Arrays.asList(License.Type.FREE, License.Type.PROFESSIONAL).contains(license.getType())) {
            dbTypeField.setValue(h2Connection);
            dbHostField.setValue("database/reporter;AUTO_SERVER=true");
            dbNameField.setValue("reporter");
            dbUserField.setValue("sa");
            dbPasswordField.setValue("123");
            adminLoginField.setValue("admin");
            adminPasswordField.setValue("123");
        }
        if (Objects.nonNull(license) && Arrays.asList(License.Type.CORPORATIVE, License.Type.BUSINESS).contains(license.getType())) {
            add(dbHostField, dbPortNumberField, dbUserField, dbPasswordField);
            add(adminLoginField, adminPasswordField);
        }
        add(createDownloadButton());
    }

    private Component createDownloadButton() {
        final var licenseTypeName = Optional.ofNullable(license)
                .map(License::getType)
                .map(Enum::name)
                .map(String::toLowerCase)
                .orElse(License.Type.FREE.name().toLowerCase());
        final var exportAnchor = new Anchor();
        exportAnchor.setHref(new StreamResource(String.format("reporter-%s.zip", licenseTypeName), () -> zipService.getZipInputStream(getProperties(), licenseTypeName)));
        exportAnchor.getElement().setAttribute("download", true);
        exportAnchor.add(downloadButton);

        return exportAnchor;
    }

    private Map<String, Object> getProperties() {
        return new HashMap<>(){{
            put("spring.profiles.active", "@spring.profiles.active@");
            put("server.port", portNumberField.getValue().intValue());
            put("vaadin.productionMode", "true");
            put("vaadin.compatibilityMode", "false");
            put("spring.datasource.url", dbTypeField.getValue().getConnectionString());
            put("spring.datasource.driverClassName", dbTypeField.getValue().getDriver());
            put("spring.datasource.username", dbTypeField.getValue().getLogin().getValue());
            put("spring.datasource.password", dbTypeField.getValue().getPassword().getValue());
            put("spring.jpa.database-platform", dbTypeField.getValue().getHibernateDialect());
            put("spring.jpa.hibernate.ddl-auto", "update");
            put("administrator.login", adminLoginField.getValue());
            put("administrator.password", adminPasswordField.getValue());
            put("jasypt.encryptor.password", "plorum");
            put("system.domain", "localhost");
            put("spring.mail.from", "dunderflute@yandex.ru");
            put("spring.mail.host", "smtp.yandex.ru");
            put("spring.mail.username", "dunderflute");
            put("spring.mail.password", "syxlqgfuuwhplcwr");
            put("spring.mail.port", "465");
            put("spring.mail.protocol", "smtps");
        }};
    }

}

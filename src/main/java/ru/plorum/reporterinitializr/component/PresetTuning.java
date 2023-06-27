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
import lombok.Setter;
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

    private final TextField dbUserField = new TextField("Пользователь БД");

    private final PasswordField dbPasswordField = new PasswordField("Пароль БД");

    private final ComboBox<Connection> dbTypeField = new ComboBox<>("Тип БД");

    private final TextField adminLoginField = new TextField("Логин администратора");

    private final PasswordField adminPasswordField = new PasswordField("Пароль администратора");

    private final Button downloadButton = new Button("Скачать");

    private final ZipService zipService;

    @Setter
    private License license;

    public PresetTuning(final ZipService zipService) {
        this.zipService = zipService;
        dbTypeField.setItemLabelGenerator(Connection::getConnectionType);
        dbTypeField.setItems(
                new H2Connection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField),
                new MSSQLConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField),
                new MYSQLConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField),
                new OracleConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField),
                new PostgresConnection(dbHostField, dbPortNumberField, dbUserField, dbPasswordField)
        );
        add(portNumberField);
        if (Objects.nonNull(license) && Arrays.asList(License.Type.FREE, License.Type.PROFESSIONAL).contains(license.getType())) {
            dbHostField.setValue("database/reporter;AUTO_SERVER=true");
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
        }};
    }

    /*
    spring.profiles.active=@spring.profiles.active@
server.port=8081
spring.devtools.restart.poll-interval=2s
spring.devtools.restart.quiet-period=1s
##vaadin
vaadin.productionMode=false
vaadin.compatibilityMode=false
#database
spring.datasource.url=jdbc:h2:file:./database/reporter;AUTO_SERVER=true
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=12345
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
#admin
administrator.login=admin
administrator.password=123
jasypt.encryptor.password=plorum
system.domain=localhost
#mail
spring.mail.from=dunderflute@yandex.ru
spring.mail.host=smtp.yandex.ru
spring.mail.username=dunderflute
spring.mail.password=syxlqgfuuwhplcwr
spring.mail.port=465
spring.mail.protocol=smtps
    * */

}

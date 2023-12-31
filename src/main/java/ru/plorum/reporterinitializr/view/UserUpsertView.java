package ru.plorum.reporterinitializr.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.plorum.reporterinitializr.model.License;
import ru.plorum.reporterinitializr.model.Role;
import ru.plorum.reporterinitializr.model.User;
import ru.plorum.reporterinitializr.service.LicenseService;
import ru.plorum.reporterinitializr.service.RoleService;
import ru.plorum.reporterinitializr.service.UserService;
import ru.plorum.reporterinitializr.util.LoginGenerator;
import ru.plorum.reporterinitializr.util.PasswordGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


@PageTitle("Пользователь")
@RequiredArgsConstructor
@RolesAllowed(value = {"ROLE_ADMIN"})
@Route(value = "users/upsert", layout = MainView.class)
public class UserUpsertView extends AbstractView implements HasUrlParameter<String>, Validatable {

    private final TextField fioField = new TextField("ФИО");

    private final TextField emailField = new TextField("Email");

    private final TextField loginField = new TextField("Логин");

    private final Button generateLoginButton = new Button("Создать логин");

    private final PasswordField passwordField = new PasswordField("Пароль");

    private final DatePicker licenseFinishDateField = new DatePicker("Дата окончания лицензии");

    private final ComboBox<License.Type> licenseTypeField = new ComboBox<>("Тип лицензии");

    private final Button generatePasswordButton = new Button("Создать пароль");

    private final MultiSelectComboBox<Role> rolesField = new MultiSelectComboBox<>("Роли");

    private final Checkbox blockField = new Checkbox("Заблокировать");

    private final Button saveButton = new Button("Сохранить");

    private Registration saveListener;

    private final RoleService roleService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final LicenseService licenseService;

    private final DatePicker.DatePickerI18n i18n;

    @Override
    @PostConstruct
    protected void initialize() {
        super.initialize();
        generateLoginButton.addClickListener(e -> {
            if (StringUtils.hasText(fioField.getValue())) {
                loginField.setValue(generateLogin(fioField.getValue()));
                return;
            }
            final var notification = Notification.show("Введите ФИО");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        });
        generatePasswordButton.addClickListener(e -> passwordField.setValue(PasswordGenerator.generate()));
        vertical.add(fioField);
        vertical.add(emailField);
        final var loginLayout = new HorizontalLayout(loginField, generateLoginButton);
        loginLayout.setAlignItems(Alignment.END);
        vertical.add(loginLayout);
        final var passwordLayout = new HorizontalLayout(passwordField, generatePasswordButton);
        passwordLayout.setAlignItems(Alignment.END);
        vertical.add(passwordLayout);
        vertical.add(createLicenseFinishDateField());
        vertical.add(createLicenseTypeField());
        rolesField.setItems(roleService.findAll());
        rolesField.setItemLabelGenerator(Role::getName);
        vertical.add(rolesField);
        vertical.add(blockField);
        vertical.add(createSaveButton());
        add(vertical);
    }

    private Component createLicenseTypeField() {
        licenseTypeField.setItems(License.Type.values());
        licenseTypeField.setItemLabelGenerator(l -> StringUtils.capitalize(l.name().toLowerCase()));
        licenseTypeField.setAllowCustomValue(false);

        return licenseTypeField;
    }

    private Component createLicenseFinishDateField() {
        licenseFinishDateField.setI18n(i18n);

        return licenseFinishDateField;
    }

    private Button createSaveButton() {
        saveListener = saveButton.addClickListener(e -> {
            if (!validate()) return;
            saveUser(new User(UUID.randomUUID()));
            saveButton.getUI().ifPresent(ui -> ui.navigate("users"));
        });
        return saveButton;
    }

    @Transactional
    protected void saveUser(final User user) {
        user.setActive(!blockField.getValue());
        user.setName(fioField.getValue());
        user.setLogin(loginField.getValue());
        user.setPassword(passwordEncoder.encode(passwordField.getValue()));
        user.setRoles(rolesField.getSelectedItems());
        user.setCreatedOn(Optional.ofNullable(user.getCreatedOn()).orElse(LocalDateTime.now()));
        user.setEmail(emailField.getValue());
        userService.save(user);
        final var license = licenseService.findByUser(user).orElse(new License(UUID.randomUUID(), user, LocalDate.now()));
        license.setType(licenseTypeField.getValue());
        license.setFinishDate(licenseFinishDateField.getValue());
        licenseService.save(license);
    }

    @Override
    public void setParameter(final BeforeEvent event, @OptionalParameter final String s) {
        final var location = event.getLocation();
        final var queryParameters = location.getQueryParameters();
        final var parametersMap = queryParameters.getParameters();
        final var id = parametersMap.getOrDefault("id", Collections.emptyList());
        if (CollectionUtils.isEmpty(id)) return;
        final var user = userService.findById(UUID.fromString(id.iterator().next()));
        if (user.isEmpty()) return;
        fioField.setValue(user.get().getName());
        Optional.ofNullable(user.get().getEmail()).ifPresent(emailField::setValue);
        loginField.setValue(user.get().getLogin());
        passwordField.setValue(user.get().getPassword());
        rolesField.select(user.get().getRoles());
        blockField.setValue(!user.get().isActive());
        licenseService.findByUser(user.get()).ifPresent(license -> {
            licenseTypeField.setValue(license.getType());
            licenseFinishDateField.setValue(license.getFinishDate());
        });
        saveListener.remove();
        saveListener = saveButton.addClickListener(e -> {
            if (!validate()) return;
            saveUser(user.get());
            saveButton.getUI().ifPresent(ui -> ui.navigate("users"));
        });
    }

    @Override
    public boolean validate() {
        final Binder<User> binder = new BeanValidationBinder<>(User.class);
        final String requiredField = "Поле обязательно к заполнению";
        binder.forField(fioField).asRequired(requiredField).bind(User::getName, User::setName);
        binder.forField(loginField).asRequired(requiredField).bind(User::getName, User::setName);
        binder.forField(passwordField).asRequired(requiredField).bind(User::getName, User::setName);
        binder.forField(blockField).withValidator(bf -> !bf || !loginField.getValue().equals(Optional.ofNullable(userService.getAuthenticatedUser()).map(User::getLogin).orElse(null)), "Нельзя заблокировать самого себя");
        return binder.validate().isOk();
    }

    private String generateLogin(final String fio) {
        final var login = LoginGenerator.generate(fio);
        if (CollectionUtils.isEmpty(userService.findByLoginLike(login))) return login;
        if (login.matches("\\D+_\\d+")) {
            return login.split("_")[0] + "_" + (Long.parseLong(login.split("_")[1]) + 1);
        }
        return login + "_1";
    }

}

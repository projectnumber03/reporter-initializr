package ru.plorum.reporterinitializr.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import ru.plorum.reporterinitializr.model.User;
import ru.plorum.reporterinitializr.service.MailService;
import ru.plorum.reporterinitializr.service.UserService;
import ru.plorum.reporterinitializr.util.LoginGenerator;
import ru.plorum.reporterinitializr.util.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@AnonymousAllowed
@Route("register")
@PageTitle("Регистрация")
public class RegisterUserView extends VerticalLayout {

    public RegisterUserView(final UserService userService, final MailService mailService) {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        final var fioField = new TextField("ФИО");
        final var emailField = new TextField("E-Mail");
        fioField.setPlaceholder("ФИО (полностью)");
        fioField.setWidth(300, Unit.PIXELS);
        emailField.setPlaceholder("E-Mail");
        emailField.setWidth(300, Unit.PIXELS);
        var processButton = new Button("Отправить заявку на регистрацию");
        processButton.addClickListener(e -> {
            final var usersWithEmail = userService.findActiveByEmails(Collections.singletonList(emailField.getValue()));
            if (!CollectionUtils.isEmpty(usersWithEmail)) {
                final var notification = Notification.show("Пользователь с данным email уже существует");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
                return;
            }
            final var user = new User();
            user.setId(UUID.randomUUID());
            user.setName(fioField.getValue());
            user.setLogin(LoginGenerator.generate(fioField.getValue()));
            final var rawPassword = PasswordGenerator.generate();
            user.setPassword(new BCryptPasswordEncoder(8).encode(rawPassword));
            user.setCreatedOn(LocalDateTime.now());
            user.setEmail(emailField.getValue());
            userService.save(user);
            mailService.sendInactive(user, rawPassword);
            UI.getCurrent().navigate("login");
        });
        add(new H1("Регистрация"), fioField, emailField, processButton);
    }

}

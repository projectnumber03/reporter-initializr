package ru.plorum.reporterinitializr.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;


@PageTitle("Главная")
@RequiredArgsConstructor
@RolesAllowed(value = {"ROLE_ADMIN"})
@Route(value = "clients", layout = MainView.class)
public class ClientView extends AbstractView {

    @Override
    @PostConstruct
    protected void initialize() {
        super.initialize();
        add(vertical);
    }

}

package ru.plorum.reporterinitializr.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Профиль")
@Route(value = "profile", layout = MainView.class)
public class ProfileView extends AbstractView {

    @Override
    @PostConstruct
    protected void initialize() {
        super.initialize();
        add(vertical);
    }

}

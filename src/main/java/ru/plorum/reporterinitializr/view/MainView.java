package ru.plorum.reporterinitializr.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import lombok.Getter;
import ru.plorum.reporterinitializr.model.Role;
import ru.plorum.reporterinitializr.service.UserService;
import ru.plorum.reporterinitializr.view.profile.ProfileView;
import ru.plorum.reporterinitializr.view.user.UserView;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.plorum.reporterinitializr.util.Constants.*;

@CssImport(value = "./css/vaadin-menu-bar-button.css", themeFor = "vaadin-menu-bar-button")
public class MainView extends AppLayout {

    private final UserService userService;

    @Getter
    private final H3 viewTitle = new H3();

    public MainView(final UserService userService) {
        this.userService = userService;

        final var user = userService.getAuthenticatedUser();

        final var toggle = new DrawerToggle();
        final var title = new H1(APP_NAME);
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        final var mainMenuHeader = new H6("Главное меню");
        mainMenuHeader.getStyle().set("padding", "10px 25px 10px 15px");
        addToDrawer(mainMenuHeader);
        addToDrawer(getMainMenuTabs());
        addToNavbar(toggle, title, createUserData());
        if (Objects.isNull(user) || user.getRoles().stream().noneMatch(r -> ROLE_ADMIN.equals(r.getName()))) return;
        final var adminMenuHeader = new H6("Панель администратора");
        adminMenuHeader.getStyle().set("padding", "10px 25px 10px 15px");
        addToDrawer(adminMenuHeader);
        addToDrawer(getAdminMenuTabs());
    }

    private Tabs getMainMenuTabs() {
        final var tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.INFO, "Главная", IndexView.class),
                createTab(VaadinIcon.USER, PROFILE, ProfileView.class),
                createTab(VaadinIcon.USERS, "Пользователи", UserView.class)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tabs getAdminMenuTabs() {
        final var tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.USERS, "Клиенты", ClientView.class)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(final VaadinIcon viewIcon, final String viewName, final Class<? extends Component> navigationTarget) {
        final var icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        final var link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setTabIndex(-1);
        Optional.ofNullable(navigationTarget).ifPresent(link::setRoute);

        return new Tab(link);
    }

    private VerticalLayout createUserData() {
        final var layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.setWidthFull();
        final var menuBar = new MenuBar();
        final var user = userService.getAuthenticatedUser();
        final var menuItem = menuBar.addItem(new Html(String.format("<div style=\"width: 400px\">%s<br/>(%s)<div>", user.getName(), user.getRoles().stream().map(Role::getDescription).collect(Collectors.joining(", ")))));
        final var logoutButton = new Button("Выйти");
        logoutButton.setThemeName("tertiary");
        logoutButton.setIcon(VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            logoutButton.getUI().ifPresent(ui -> ui.getPage().reload());
        });
        menuItem.getSubMenu().addItem(logoutButton);
        layout.add(menuBar);

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }

}

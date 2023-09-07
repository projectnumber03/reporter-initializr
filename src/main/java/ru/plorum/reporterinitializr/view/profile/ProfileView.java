package ru.plorum.reporterinitializr.view.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.util.StringUtils;
import ru.plorum.reporterinitializr.component.ConfirmationDialog;
import ru.plorum.reporterinitializr.component.NewButton;
import ru.plorum.reporterinitializr.component.pagination.PaginatedGrid;
import ru.plorum.reporterinitializr.model.Client;
import ru.plorum.reporterinitializr.model.Requisite;
import ru.plorum.reporterinitializr.service.ClientService;
import ru.plorum.reporterinitializr.service.UserService;
import ru.plorum.reporterinitializr.view.AbstractView;
import ru.plorum.reporterinitializr.view.MainView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.plorum.reporterinitializr.util.Constants.*;

@PermitAll
@PageTitle(PROFILE)
@Route(value = "profile", layout = MainView.class)
public class ProfileView extends AbstractView {

    private final UserService userService;

    private final ClientService clientService;

    private final PaginatedGrid<Client> clientTable;

    public ProfileView(final UserService userService, final ClientService clientService) {
        this.userService = userService;
        this.clientService = clientService;
        this.clientTable = createClientTable();
    }

    @Override
    @PostConstruct
    protected void initialize() {
        final var layout = new VerticalLayout(new H4("Настройка профиля"), createNewButton());
        layout.setPadding(false);
        horizontal.add(layout);
        vertical.add(clientTable);
        add(vertical);
    }

    private Component createNewButton() {
        return new NewButton("Новый профиль", "profile/upsert");
    }

    private PaginatedGrid<Client> createClientTable() {
        final Grid<Client> grid = new Grid<>();
        grid.addColumn(createEditButtonRenderer()).setHeader(PROFILE);
        grid.addColumn(c -> c.getType().getName()).setHeader(TYPE);
        grid.addColumn(Client::getId).setHeader("Код клиента");
        createContextMenu(grid);
        final var paginatedGrid = new PaginatedGrid<>(grid);
        paginatedGrid.setItems(clientService.findAllByUser(userService.getAuthenticatedUser()));
        return paginatedGrid;
    }

    private ComponentRenderer<Button, Client> createEditButtonRenderer() {
        final SerializableBiConsumer<Button, Client> editButtonProcessor = (button, client) -> {
            button.setThemeName("tertiary");
            button.setText(getClientNameByType(client));
            button.addClickListener(e -> button.getUI().ifPresent(ui -> ui.navigate("profile/upsert/", getQueryParameters(client))));
        };
        return new ComponentRenderer<>(Button::new, editButtonProcessor);
    }

    private QueryParameters getQueryParameters(final Client client) {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("id", client.getId());
        return QueryParameters.simple(parameters);
    }

    private void createContextMenu(final Grid<Client> grid) {
        final var menu = grid.addContextMenu();
        final var editItem = menu.addItem(EDIT);
        editItem.addMenuItemClickListener(event -> event.getItem().ifPresent(client -> editItem.getUI().ifPresent(ui -> ui.navigate("profile/upsert/", getQueryParameters(client)))));
        menu.addItem(DELETE, event -> {
            event.getItem().ifPresent(client -> {
                final Runnable callback = () -> {
                    clientService.delete(client);
                    event.getGrid().setItems(clientService.findAllByUser(userService.getAuthenticatedUser()));
                };
                new ConfirmationDialog(String.format("Хотите удалить клиента \"%s\"?", getClientNameByType(client)), callback).open();
            });
        });
    }

    private String getClientNameByType(final Client client) {
        if (client.getType() == Client.Type.JURIDICAL) {
            return Optional.ofNullable(client.getRequisites()).map(Requisite::getName).orElse(NA);
        }
        if (client.getType() == Client.Type.PHYSICAL) {
            return String.format("%s %s.%s", client.getSecondName(), client.getFirstName().toUpperCase().charAt(0), Optional.ofNullable(client.getLastName()).filter(StringUtils::hasText).map(String::toUpperCase).map(v -> v.charAt(0) + ".").orElse(""));
        }
        return "";
    }

}

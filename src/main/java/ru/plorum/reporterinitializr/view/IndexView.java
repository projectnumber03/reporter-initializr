package ru.plorum.reporterinitializr.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import ru.plorum.reporterinitializr.component.PresetTuning;
import ru.plorum.reporterinitializr.service.LicenseService;
import ru.plorum.reporterinitializr.service.UserService;
import ru.plorum.reporterinitializr.service.ZipService;


@PermitAll
@PageTitle("Главная")
@RequiredArgsConstructor
@Route(value = "", layout = MainView.class)
public class IndexView extends AbstractView {

    private final UserService userService;

    private final LicenseService licenseService;

    private final ZipService zipService;

    private PresetTuning presetTuning = new PresetTuning(zipService);

    @Override
    @PostConstruct
    protected void initialize() {
        final var authenticatedUser = userService.getAuthenticatedUser();
        final var license = licenseService.findByUser(authenticatedUser);
        license.ifPresent(presetTuning::setLicense);
        vertical.add(presetTuning);
        add(vertical);
    }

}

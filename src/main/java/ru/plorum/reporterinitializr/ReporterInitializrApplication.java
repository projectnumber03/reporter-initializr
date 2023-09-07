package ru.plorum.reporterinitializr;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Push
@SpringBootApplication
@Theme(value = "common-theme", variant = Lumo.LIGHT)
public class ReporterInitializrApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ReporterInitializrApplication.class, args);
    }

}

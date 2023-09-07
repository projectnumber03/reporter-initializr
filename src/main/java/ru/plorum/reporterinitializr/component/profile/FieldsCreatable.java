package ru.plorum.reporterinitializr.component.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public interface FieldsCreatable {

    VerticalLayout createFields();

    default Label createLabel(final String text) {
        final var label = new Label(text);
        label.getStyle().set("font-size", "85%");
        return label;
    }

    default HorizontalLayout createHorizontalLayout(final Component... components) {
        final var horizontalLayout = new HorizontalLayout(components);
        horizontalLayout.setPadding(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return horizontalLayout;
    }

    record TextFieldDTO(TextField field, String text) {}

}

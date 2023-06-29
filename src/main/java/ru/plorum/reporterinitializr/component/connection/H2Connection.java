package ru.plorum.reporterinitializr.component.connection;


import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class H2Connection extends Connection {

    public H2Connection(final TextField host, final NumberField port, final TextField login, final PasswordField password, final TextField name) {
        super(host, port, login, password, name);
    }

    @Override
    public String getConnectionString() {
        return String.format("jdbc:h2:file:./%s", getName());
    }

    @Override
    public String getConnectionType() {
        return "H2";
    }

    @Override
    public String getHibernateDialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    @Override
    public String getDriver() {
        return "org.h2.Driver";
    }

}

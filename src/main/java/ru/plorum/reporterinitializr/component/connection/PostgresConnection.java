package ru.plorum.reporterinitializr.component.connection;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class PostgresConnection extends Connection {

    public PostgresConnection(final TextField host, final NumberField port, final TextField login, final PasswordField password) {
        super(host, port, login, password);
    }

    @Override
    public String getConnectionString() {
        return String.format("jdbc:postgresql://%s:%s/%s", getHost().getValue(), getPort(), getName());
    }

    @Override
    public String getConnectionType() {
        return "POSTGRESQL";
    }

    @Override
    public String getHibernateDialect() {
        return "org.hibernate.dialect.PostgreSQLDialect";
    }

    @Override
    public String getDriver() {
        return "org.postgresql.Driver";
    }

}

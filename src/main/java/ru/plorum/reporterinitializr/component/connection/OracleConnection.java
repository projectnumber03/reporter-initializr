package ru.plorum.reporterinitializr.component.connection;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class OracleConnection extends Connection {

    public OracleConnection(final TextField host, final NumberField port, final TextField login, final PasswordField password, final TextField name) {
        super(host, port, login, password, name);
    }

    @Override
    public String getConnectionString() {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", getHost().getValue(), getPort(), getName());
    }

    @Override
    public String getConnectionType() {
        return "ORACLE";
    }

    @Override
    public String getHibernateDialect() {
        return "org.hibernate.dialect.OracleDialect";
    }

    @Override
    public String getDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }

}

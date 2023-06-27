package ru.plorum.reporterinitializr.component.connection;


import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MYSQLConnection extends Connection {

    public MYSQLConnection(final TextField host, final NumberField port, final TextField login, final PasswordField password) {
        super(host, port, login, password);
    }

    @Override
    public String getConnectionString() {
        return String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", getHost().getValue(), getPort(), getName());
    }

    @Override
    public String getConnectionType() {
        return "MYSQL";
    }

    @Override
    public String getHibernateDialect() {
        return "org.hibernate.dialect.MySQLDialect";
    }

    @Override
    public String getDriver() {
        return "com.mysql.cj.jdbc.Driver";
    }

}

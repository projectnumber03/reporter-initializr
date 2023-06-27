package ru.plorum.reporterinitializr.component.connection;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Connection {

    TextField host;

    NumberField port;

    TextField login;

    PasswordField password;

    String name;

    public Connection(final TextField host, final NumberField port, final TextField login, final PasswordField password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public abstract String getConnectionString();

    public abstract String getConnectionType();

    public abstract String getHibernateDialect();

    public abstract String getDriver();

    public static Connection getByDriver(final String driver) {
        return switch (driver) {
            case "com.microsoft.sqlserver.jdbc.SQLServerDriver" -> new MSSQLConnection();
            case "com.mysql.cj.jdbc.Driver" -> new MYSQLConnection();
            case "oracle.jdbc.driver.OracleDriver" -> new OracleConnection();
            case "org.postgresql.Driver" -> new PostgresConnection();
            default -> new H2Connection();
        };
    }

}

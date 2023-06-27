package ru.plorum.reporterinitializr.component.connection;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;


@NoArgsConstructor
public final class MSSQLConnection extends Connection {

    public MSSQLConnection(final TextField host, final NumberField port, final TextField login, final PasswordField password) {
        super(host, port, login, password);
    }

    @Override
    public String getConnectionString() {
        //Если несколько инстансов
        if (StringUtils.hasText(getHost().getValue()) && getHost().getValue().matches(".+\\\\.+")) {
            return String.format("jdbc:sqlserver://%s;database=%s", getHost().getValue(), getName());
        }
        //Если один
        return String.format("jdbc:sqlserver://%s:%s;database=%s", getHost().getValue(), getPort(), getName());
    }

    @Override
    public String getConnectionType() {
        return "MSSQL";
    }

    @Override
    public String getHibernateDialect() {
        return "org.hibernate.dialect.SQLServer2012Dialect";
    }

    @Override
    public String getDriver() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

}

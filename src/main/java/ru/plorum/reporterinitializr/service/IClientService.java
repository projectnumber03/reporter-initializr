package ru.plorum.reporterinitializr.service;

import ru.plorum.reporterinitializr.component.profile.FieldsCreatable;
import ru.plorum.reporterinitializr.model.Client;

public interface IClientService {

    boolean check(final Client.Type type);

    void save(final FieldsCreatable fields);

}

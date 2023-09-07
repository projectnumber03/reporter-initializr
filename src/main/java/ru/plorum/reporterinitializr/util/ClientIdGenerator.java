package ru.plorum.reporterinitializr.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.stream.Stream;

public class ClientIdGenerator implements IdentifierGenerator {

    private final static String PREFIX = "#";

    @Override
    public Serializable generate(final SharedSessionContractImplementor session, final Object obj) throws HibernateException {
        final var query = String.format("select %s from %s", session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(), obj.getClass().getSimpleName());
        final Stream<String> ids = session.createQuery(query, String.class).stream();
        final var max = ids.map(o -> o.replace(PREFIX, "").replace("-", ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);
        final LinkedList<String> result = new LinkedList<>();
        recursion(max + 1, result);
        return PREFIX + String.join("-", result);
    }

    public void recursion(final long number, final LinkedList<String> parts) {
        if (number <= 0 && parts.size() > 2) return;
        parts.addFirst(String.format("%03d", number % 1000));
        recursion(number / 1000, parts);
    }

}

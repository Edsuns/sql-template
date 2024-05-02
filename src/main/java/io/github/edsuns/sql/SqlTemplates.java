package io.github.edsuns.sql;

import io.github.edsuns.sql.impl.SelectTemplateImpl;
import io.github.edsuns.sql.impl.UpdateTemplateImpl;
import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.statement.SelectTemplate;
import io.github.edsuns.sql.statement.UpdateTemplate;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:49
 */
@ParametersAreNonnullByDefault
public class SqlTemplates {

    public static <T extends Entity, Q extends Query> UpdateTemplate<T, Q> update(Class<T> entity, Class<Q> query) {
        UpdateTemplate<T, Q> template = new UpdateTemplateImpl<>(entity, query);
        return template.update(entity);
    }

    public static <T extends Entity, Q extends Query> SelectTemplate<T, Q> select(Class<T> entity, Class<Q> query) {
        SelectTemplate<T, Q> template = new SelectTemplateImpl<>(entity, query);
        return template.select(entity, null);
    }

    public static <T extends Entity, Q extends Query, X> SelectTemplate<T, Q> select(
            Class<T> entity, Class<Q> query, Collection<SerializableFunction<T, X>> columns) {
        SelectTemplate<T, Q> template = new SelectTemplateImpl<>(entity, query);
        return template.select(entity, columns);
    }

    public static <Q extends Query, X> X nullValue(Q query) {
        return null;
    }
}

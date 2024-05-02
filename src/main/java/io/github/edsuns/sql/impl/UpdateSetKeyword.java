package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.statement.UpdateSet;
import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;
import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:09
 */
@ParametersAreNonnullByDefault
class UpdateSetKeyword<T extends Entity, Q extends Query> implements Keyword<Q>, UpdateSet<T, Q> {
    private final Queue<String> columnNames;
    private final Queue<SerializableFunction<Q, ?>> queryFields;

    public UpdateSetKeyword(Queue<String> columnNames, Queue<SerializableFunction<Q, ?>> queryFields) {
        this.columnNames = columnNames;
        this.queryFields = queryFields;
    }

    @Override
    public <X> UpdateSet<T, Q> assign(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        columnNames.add(SqlUtil.getColumnName(field));
        queryFields.add(val);
        return this;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        appendSpaceIfNotPresent(sql);
        sql.append(KEY_WORD);
        boolean affected = false;
        Iterator<SerializableFunction<Q, ?>> iterator = queryFields.iterator();
        for (String columnName : columnNames) {
            SerializableFunction<Q, ?> field = requireNonNull(iterator.next());
            Object val = field.apply(query);

            if (affected) {
                sql.append(',');
            }
            sql.append(' ');
            sql.append(columnName).append('=').append(val != null ? SqlTemplate.PLACEHOLDER : NullKeyword.KEY_WORD);
            variableConsumer.accept(val != null ? val : NullKeyword.INSTANCE);
            affected = true;
        }
        return affected;
    }
}

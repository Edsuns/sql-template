package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.statement.UpdateSet;
import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;
import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:09
 */
@ParametersAreNonnullByDefault
class UpdateSetKeyword<T extends Entity, Q extends Query> implements Keyword<T, Q>, KeywordWrite<T, Q>, UpdateSet<T, Q> {
    private final Queue<String> columnNames;
    private final Queue<SerializableFunction<Q, ?>> queryFields;
    private boolean selective = false;
    @Nullable
    private Map<String, Field> entityFields = null;

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

    public void setSelective(boolean setSelective, List<Field> entityFields) {
        this.selective = setSelective;
        this.entityFields = entityFields.stream().collect(Collectors.toMap(SqlUtil::getColumnName, x -> x, (a, b) -> a, TreeMap::new));
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        return this.parseIntoSql(sql, variableConsumer, null, query);
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable T entity, @Nullable Q query) {
        appendSpaceIfNotPresent(sql);
        sql.append(KEY_WORD);
        boolean affected = false;
        // selective first
        if (selective && entity != null && entityFields != null && !entityFields.isEmpty()) {
            for (Map.Entry<String, Field> x : entityFields.entrySet()) {
                String columnName = x.getKey();
                Field field = x.getValue();
                Object val = SqlUtil.value(entity, field);
                if (val != null) {
                    append(sql, variableConsumer, columnName, val, affected);
                }
                affected = true;
            }
        }
        // selective at last for higher priority
        Iterator<SerializableFunction<Q, ?>> iterator = queryFields.iterator();
        for (String columnName : columnNames) {
            SerializableFunction<Q, ?> field = requireNonNull(iterator.next());
            Object val = field.apply(query);
            append(sql, variableConsumer, columnName, val, affected);
            affected = true;
        }
        return affected;
    }

    private static void append(StringBuilder sql, Consumer<Object> variableConsumer,
                               String columnName, @Nullable Object val, boolean affected) {
        if (affected) {
            sql.append(',');
        }
        sql.append(' ');
        sql.append(columnName).append('=').append(val != null ? SqlTemplate.PLACEHOLDER : NullKeyword.KEY_WORD);
        variableConsumer.accept(val != null ? val : NullKeyword.INSTANCE);
    }
}

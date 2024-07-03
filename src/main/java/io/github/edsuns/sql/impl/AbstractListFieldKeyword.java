package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 18:14
 */
@ParametersAreNonnullByDefault
abstract class AbstractListFieldKeyword<T extends Entity, Q, X> implements Keyword<T, Q> {
    protected final SerializableFunction<T, X> entityField;
    protected final SerializableFunction<Q, List<X>> queryField;
    protected final String columnName;

    public AbstractListFieldKeyword(SerializableFunction<T, X> entityField, SerializableFunction<Q, List<X>> queryField) {
        this.entityField = entityField;
        this.queryField = queryField;
        this.columnName = SqlUtil.getColumnName(entityField);
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        List<X> fieldValue = queryField.apply(query);
        if (fieldValue == null) {
            return false;
        }
        parseIntoSql(fieldValue, sql, variableConsumer);
        acceptVar(variableConsumer, fieldValue);
        return true;
    }

    protected abstract void parseIntoSql(List<X> fieldValue, StringBuilder sql, Consumer<Object> variableConsumer);

    protected abstract void acceptVar(Consumer<Object> variableConsumer, List<X> fieldValue);
}

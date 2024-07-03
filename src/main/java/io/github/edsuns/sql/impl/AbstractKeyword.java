package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 13:56
 */
@ParametersAreNonnullByDefault
abstract class AbstractKeyword<T extends Entity, Q, X> implements Keyword<T, Q> {
    protected final SerializableFunction<T, X> entityField;
    protected final SerializableFunction<Q, X> queryField;
    protected final String columnName;

    protected AbstractKeyword(SerializableFunction<T, X> entityField, SerializableFunction<Q, X> queryField) {
        this.entityField = entityField;
        this.queryField = queryField;
        this.columnName = SqlUtil.getColumnName(entityField);
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        X fieldValue = queryField.apply(query);
        if (fieldValue == null) {
            throw new IllegalArgumentException("field required in query object: " + columnName);
        }
        parseIntoSql(fieldValue, sql, variableConsumer);
        acceptVar(variableConsumer, fieldValue);
        return true;
    }

    protected void acceptVar(Consumer<Object> variableConsumer, X fieldValue) {
        variableConsumer.accept(fieldValue);
    }

    protected abstract void parseIntoSql(X fieldValue, StringBuilder sql, Consumer<Object> variableConsumer);

}

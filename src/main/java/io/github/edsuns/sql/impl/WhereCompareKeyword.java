package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.statement.WhereCondition;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendAndIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 14:13
 */
@ParametersAreNonnullByDefault
class WhereCompareKeyword<T, Q, X> extends AbstractKeyword<T, Q, X> implements WhereCondition {
    private final char expression;

    protected WhereCompareKeyword(SerializableFunction<T, X> entityField, SerializableFunction<Q, X> queryField, char expression) {
        super(entityField, queryField);
        this.expression = expression;
    }

    @Override
    protected void parseIntoSql(X fieldValue, StringBuilder sql, Consumer<Object> variableConsumer) {
        appendAndIfNotPresent(sql);
        sql.append(columnName).append(expression).append(SqlTemplate.PLACEHOLDER);
    }
}

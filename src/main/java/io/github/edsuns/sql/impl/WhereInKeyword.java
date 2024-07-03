package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.statement.WhereCondition;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.edsuns.sql.statement.Where.KEY_WORD_IN;
import static io.github.edsuns.sql.util.SqlUtil.appendAndIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 14:16
 */
@ParametersAreNonnullByDefault
class WhereInKeyword<T extends Entity, Q, X> extends AbstractListFieldKeyword<T, Q, X> implements WhereCondition {

    public WhereInKeyword(SerializableFunction<T, X> entityField, SerializableFunction<Q, List<X>> queryField) {
        super(entityField, queryField);
    }

    @Override
    protected void parseIntoSql(List<X> fieldValue, StringBuilder sql, Consumer<Object> variableConsumer) {
        appendAndIfNotPresent(sql);
        sql.append(columnName);
        sql.append(' ').append(KEY_WORD_IN).append(' ').append('(')
                .append(IntStream.range(0, fieldValue.size()).mapToObj(i -> SqlTemplate.PLACEHOLDER)
                        .collect(Collectors.joining(","))
                )
                .append(')');
    }

    @Override
    protected void acceptVar(Consumer<Object> variableConsumer, List<X> fieldValue) {
        for (X var : fieldValue) {
            variableConsumer.accept(var);
        }
    }
}

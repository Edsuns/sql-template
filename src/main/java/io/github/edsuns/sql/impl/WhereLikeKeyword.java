package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.statement.WhereCondition;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendAndIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 14:15
 */
@ParametersAreNonnullByDefault
class WhereLikeKeyword<T, Q, X> extends AbstractKeyword<T, Q, X> implements WhereCondition {
    public static final String KEY_WORD = "LIKE";

    private final boolean prefixMatching;

    protected WhereLikeKeyword(SerializableFunction<T, X> entityField, SerializableFunction<Q, X> queryField, boolean prefixMatching) {
        super(entityField, queryField);
        this.prefixMatching = prefixMatching;
    }

    @Override
    protected void parseIntoSql(X fieldValue, StringBuilder sql, Consumer<Object> variableConsumer) {
        appendAndIfNotPresent(sql);
        sql.append(columnName).append(' ').append(KEY_WORD).append(' ').append(SqlTemplate.PLACEHOLDER);
    }

    @Override
    protected void acceptVar(Consumer<Object> variableConsumer, X fieldValue) {
        if (prefixMatching) {
            variableConsumer.accept(fieldValue + "%");
        } else {
            variableConsumer.accept("%" + fieldValue + "%");
        }
    }
}

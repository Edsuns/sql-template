package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.statement.SelectTemplate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 18:04
 */
@ParametersAreNonnullByDefault
class SelectFromKeyword<T extends Entity, Q, X> implements Keyword<T, Q> {
    private final String tableName;
    private final String selectExpressions;
    private final boolean all;
    private final boolean distinct;
    private final boolean distinctrow;

    protected SelectFromKeyword(String tableName, String selectExpressions,
                                boolean all, boolean distinct, boolean distinctrow) {
        this.tableName = tableName;
        this.selectExpressions = selectExpressions;
        this.all = all;
        this.distinct = distinct;
        this.distinctrow = distinctrow;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        sql.append(SelectTemplate.KEY_WORD_SELECT);
        if (all) {
            sql.append(' ').append(SelectTemplate.KEY_WORD_ALL);
        }
        if (distinct) {
            sql.append(' ').append(SelectTemplate.KEY_WORD_DISTINCT);
        }
        if (distinctrow) {
            sql.append(' ').append(SelectTemplate.KEY_WORD_DISTINCTROW);
        }
        sql.append(' ').append(selectExpressions).append(' ').append(SelectTemplate.KEY_WORD_FROM).append(' ').append(tableName);
        return true;
    }
}

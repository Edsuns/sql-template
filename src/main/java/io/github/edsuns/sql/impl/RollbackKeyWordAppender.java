package io.github.edsuns.sql.impl;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 19:57
 */
@ParametersAreNonnullByDefault
class RollbackKeyWordAppender<Q> {
    private final StringBuilder sql;
    private final int[] beforeIndex;

    public RollbackKeyWordAppender(StringBuilder sql) {
        this.sql = sql;
        this.beforeIndex = new int[3];
        mark();
    }

    public boolean append(Keyword<Q> keyword, @Nullable Q query, Consumer<Object> variableConsumer) {
        if (query == null) {
            return false;
        }
        Queue<Object> variables = new ArrayDeque<>();
        boolean affected = keyword.parseIntoSql(sql, variables::add, query);
        if (affected) {
            for (Object val : variables) {
                if (val != NullKeyword.KEY_WORD) {
                    variableConsumer.accept(val);
                }
            }
        }
        return affected;
    }

    public void mark() {
        this.beforeIndex[0] = sql.length();
    }

    public void mark(int savepoint) {
        this.beforeIndex[savepoint] = sql.length();
    }

    public void rollback() {
        if (beforeIndex[0] < sql.length()) {
            sql.delete(beforeIndex[0], sql.length());
        }
    }

    public void rollback(int savepoint) {
        if (beforeIndex[savepoint] < sql.length()) {
            sql.delete(beforeIndex[savepoint], sql.length());
        }
    }
}

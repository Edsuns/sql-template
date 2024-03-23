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
            for (Object var : variables) {
                variableConsumer.accept(var);
            }
        }
        return affected;
    }

    public void mark() {
        this.beforeIndex[0] = sql.length();
    }

    public void mark(int key) {
        this.beforeIndex[key] = sql.length();
    }

    public void rollback() {
        if (beforeIndex[0] < sql.length()) {
            sql.delete(beforeIndex[0], sql.length());
        }
    }

    public void rollback(int key) {
        if (beforeIndex[key] < sql.length()) {
            sql.delete(beforeIndex[key], sql.length());
        }
    }
}

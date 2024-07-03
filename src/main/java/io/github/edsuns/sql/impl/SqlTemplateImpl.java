package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.*;
import io.github.edsuns.sql.util.StringBuilderPool;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 10:11
 */
@ParametersAreNonnullByDefault
class SqlTemplateImpl<T extends Entity, Q extends Query, R> implements ReadStatement<T, Q, R>, WriteStatement<T, Q, R> {

    private final Queue<Keyword<T, Q>> keywords;
    @Nullable
    protected final Class<R> resultClass;

    public SqlTemplateImpl(Queue<Keyword<T, Q>> keywords, @Nullable Class<R> resultClass) {
        this.keywords = keywords;
        this.resultClass = resultClass;
    }

    @Override
    public Sql generateSql(@Nullable Q query) {
        return this.generateSql(null, query);
    }

    @Override
    public Sql generateSql(@Nullable T entity, @Nullable Q query) {
        Queue<Object> vars = new ArrayDeque<>();
        StringBuilder sql = StringBuilderPool.get();
        RollbackKeyWordAppender<T, Q> appender = new RollbackKeyWordAppender<>(sql);
        for (Keyword<T, Q> keyword : keywords) {
            appender.mark();
            if (!appender.append(keyword, entity, query, vars::add)) {
                appender.rollback();
            }
        }
        return new SqlImpl(sql.toString(), vars);
    }

    @Override
    public R execute(SqlExecutor executor, @Nullable Q query) {
        Sql sql = generateSql(query);
        List<R> result = executor.read(sql.getSqlTemplateString(), sql.getVariables(), requireNonNull(resultClass));
        return result == null || result.isEmpty() ? null : result.get(0);
    }
}

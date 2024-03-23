package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.Sql;
import io.github.edsuns.sql.protocol.SqlExecutor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:39
 */
@ParametersAreNonnullByDefault
class ListSqlTemplateImpl<T extends Entity, Q extends Query> extends SqlTemplateImpl<T, Q, List<T>> {
    private final Class<T> entityClass;

    public ListSqlTemplateImpl(Queue<Keyword<Q>> keywords, Class<T> entityClass) {
        super(keywords, null);
        this.entityClass = entityClass;
    }

    @Override
    public List<T> execute(SqlExecutor executor, @Nullable T entity, @Nullable Q query) {
        Sql sql = generateSql(entity, query);
        return executor.read(sql.getSqlTemplateString(), sql.getVariables(), entityClass);
    }
}

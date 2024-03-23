package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.Sql;
import io.github.edsuns.sql.protocol.SqlExecutor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:51
 */
@ParametersAreNonnullByDefault
class AffectedSqlTemplateImpl<T extends Entity, Q extends Query> extends SqlTemplateImpl<T, Q, Long> {
    public AffectedSqlTemplateImpl(Queue<Keyword<Q>> keywords) {
        super(keywords, Long.class);
    }

    @Override
    public Long execute(SqlExecutor executor, @Nullable T entity, @Nullable Q query) {
        Sql sql = generateSql(entity, query);
        return executor.write(sql.getSqlTemplateString(), sql.getVariables());
    }
}

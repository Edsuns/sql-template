package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:51
 */
@ParametersAreNonnullByDefault
class AffectedSqlTemplateImpl<T extends Entity, Q extends Query> extends SqlTemplateImpl<T, Q, Long> implements WriteStatement<T, Q, Long> {
    public AffectedSqlTemplateImpl(Queue<Keyword<T, Q>> keywords) {
        super(keywords, Long.class);
    }

    @Override
    public Long execute(SqlExecutor executor, @Nullable Q query) {
        Sql sql = generateSql(query);
        return executor.write(sql.getSqlTemplateString(), sql.getVariables());
    }
}

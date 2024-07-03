package io.github.edsuns.sql.protocol;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/7/3 16:13
 */
public interface WriteStatement<T extends Entity, Q extends Query, R> extends SqlTemplate<T, Q, R> {

    Sql generateSql(@Nullable T entity, @Nullable Q query);

    default R execute(@Nullable T entity, @Nullable Q query) {
        return this.execute(requireNonNull(DefaultSqlExecutor.INSTANCE), entity, query);
    }

    R execute(SqlExecutor executor, @Nullable T entity, @Nullable Q query);
}

package io.github.edsuns.sql.protocol;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 9:33
 */
@ParametersAreNonnullByDefault
public interface SqlTemplate<T extends Entity, Q extends Query, R> {

    String PLACEHOLDER = "?";

    Sql generateSql(@Nullable T entity, @Nullable Q query);

    default R execute(@Nullable T entity, @Nullable Q query) {
        return this.execute(requireNonNull(DefaultSqlExecutor.INSTANCE), entity, query);
    }

    R execute(SqlExecutor executor, @Nullable T entity, @Nullable Q query);
}

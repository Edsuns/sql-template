package io.github.edsuns.sql.protocol;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/7/3 16:14
 */
public interface ReadStatement<T extends Entity, Q extends Query, R> extends SqlTemplate<T, Q, R> {

    Sql generateSql(@Nullable Q query);

    default R execute(@Nullable Q query) {
        return this.execute(requireNonNull(DefaultSqlExecutor.INSTANCE), query);
    }

    R execute(SqlExecutor executor, @Nullable Q query);
}

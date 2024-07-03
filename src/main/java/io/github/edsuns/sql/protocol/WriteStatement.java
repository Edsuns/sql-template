package io.github.edsuns.sql.protocol;

import javax.annotation.Nullable;

/**
 * @author edsuns@qq.com
 * @since 2024/7/3 16:13
 */
public interface WriteStatement<T extends Entity, Q extends Query, R> extends SqlTemplate<T, Q, R> {

    Sql generateSql(@Nullable T entity, @Nullable Q query);
}

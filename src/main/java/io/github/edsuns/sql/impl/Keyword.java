package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 13:54
 */
@ParametersAreNonnullByDefault
interface Keyword<T extends Entity, Q> {
    /**
     * for read statement
     */
    boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query);
}

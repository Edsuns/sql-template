package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/7/3 16:33
 */
interface KeywordWrite<T extends Entity, Q> extends Keyword<T, Q> {

    /**
     * for write statement
     */
    boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable T entity, @Nullable Q query);
}

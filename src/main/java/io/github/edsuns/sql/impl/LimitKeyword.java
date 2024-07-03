package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 19:28
 */
@ParametersAreNonnullByDefault
class LimitKeyword<T extends Entity, Q, X extends Number> implements Keyword<T, Q> {
    public static final String KEYWORD = "LIMIT";

    @Nullable
    private final SerializableFunction<Q, X> offset;
    private final SerializableFunction<Q, X> rows;

    public LimitKeyword(@Nullable SerializableFunction<Q, X> offset, SerializableFunction<Q, X> rows) {
        this.offset = offset;
        this.rows = rows;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        appendSpaceIfNotPresent(sql);
        sql.append(KEYWORD).append(' ');
        if (offset != null) {
            sql.append(offset.apply(query)).append(',');
        }
        sql.append(rows.apply(query));
        return true;
    }
}

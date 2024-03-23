package io.github.edsuns.sql.impl;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendAndIfNotPresent;
import static io.github.edsuns.sql.util.SqlUtil.appendOrIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 14:21
 */
@ParametersAreNonnullByDefault
class WhereAndOrKeyword<T, Q, X> implements Keyword<Q> {
    private final boolean and;

    public WhereAndOrKeyword(boolean and) {
        this.and = and;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (and) {
            appendAndIfNotPresent(sql);
        } else {
            appendOrIfNotPresent(sql);
        }
        return true;
    }
}

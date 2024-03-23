package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.statement.UpdateTemplate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 20:51
 */
@ParametersAreNonnullByDefault
class UpdateKeyword<T, Q, X> implements Keyword<Q> {
    private final String tableName;
    private final boolean lowPriority;
    private final boolean ignore;

    public UpdateKeyword(String tableName, boolean lowPriority, boolean ignore) {
        this.tableName = tableName;
        this.lowPriority = lowPriority;
        this.ignore = ignore;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        appendSpaceIfNotPresent(sql);
        sql.append(UpdateTemplate.KEY_WORD_UPDATE);
        if (lowPriority) {
            sql.append(' ').append(UpdateTemplate.KEY_WORD_LOW_PRIORITY);
        }
        if (ignore) {
            sql.append(' ').append(UpdateTemplate.KEY_WORD_IGNORE);
        }
        sql.append(' ').append(tableName);
        return true;
    }
}

package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.statement.WhereCondition;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 15:13
 */
@ParametersAreNonnullByDefault
class WhereInnerImpl<T extends Entity, Q extends Query> extends WhereImpl<T, Q> {
    public WhereInnerImpl(Queue<Keyword<T, Q>> keyWords) {
        super(keyWords);
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        RollbackKeyWordAppender<T, Q> appender = new RollbackKeyWordAppender<>(sql);
        boolean affected = false;
        Iterator<Keyword<T, Q>> iterator = keywords.iterator();
        while (iterator.hasNext()) {
            Keyword<T, Q> next = iterator.next();
            if (next instanceof WhereAndOrKeyword) {
                if (!iterator.hasNext()) {
                    break;
                }
                Keyword<T, Q> prev = next;
                next = iterator.next();
                if (next instanceof WhereCondition) {
                    appender.mark();
                    appender.append(prev, null, query, variableConsumer);
                    if (!appender.append(next, null, query, variableConsumer)) {
                        appender.rollback();
                    }
                    continue;
                }
            }
            if (appender.append(next, null, query, variableConsumer)) {
                affected = true;
            }
        }
        return affected;
    }
}

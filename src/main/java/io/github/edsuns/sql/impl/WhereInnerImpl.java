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
class WhereInnerImpl<T extends Entity, Q extends Query> extends WhereImpl<T, Q> implements Keyword<Q> {
    public WhereInnerImpl(Queue<Keyword<Q>> keyWords) {
        super(keyWords);
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        RollbackKeyWordAppender<Q> appender = new RollbackKeyWordAppender<>(sql);
        boolean affected = false;
        Iterator<Keyword<Q>> iterator = keywords.iterator();
        while (iterator.hasNext()) {
            Keyword<Q> next = iterator.next();
            if (next instanceof WhereAndOrKeyword) {
                if (!iterator.hasNext()) {
                    break;
                }
                Keyword<Q> prev = next;
                next = iterator.next();
                if (next instanceof WhereCondition) {
                    appender.mark();
                    appender.append(prev, query, variableConsumer);
                    if (!appender.append(next, query, variableConsumer)) {
                        appender.rollback();
                    }
                    continue;
                }
            }
            if (appender.append(next, query, variableConsumer)) {
                affected = true;
            }
        }
        return affected;
    }
}

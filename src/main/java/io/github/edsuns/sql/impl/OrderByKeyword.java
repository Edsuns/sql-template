package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.statement.OrderBy;
import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;
import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 19:23
 */
@ParametersAreNonnullByDefault
class OrderByKeyword<T extends Entity, Q extends Query> implements Keyword<T, Q>, OrderBy<T> {

    private final Queue<String> columns;
    private final Queue<Boolean> desc;

    public OrderByKeyword(Queue<String> columns, Queue<Boolean> desc) {
        this.columns = columns;
        this.desc = desc;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        appendSpaceIfNotPresent(sql);
        sql.append("ORDER BY");
        boolean affected = false;
        Iterator<Boolean> iterator = desc.iterator();
        for (String columnName : columns) {
            if (affected) {
                sql.append(',');
            }
            boolean next = requireNonNull(iterator.next());
            sql.append(' ').append(columnName);
            if (next) {
                sql.append(' ').append("DESC");
            }
            affected = true;
        }
        return affected;
    }

    @Override
    public <X> OrderBy<T> asc(SerializableFunction<T, X> field) {
        columns.add(SqlUtil.getColumnName(field));
        desc.add(false);
        return this;
    }

    @Override
    public <X> OrderBy<T> desc(SerializableFunction<T, X> field) {
        columns.add(SqlUtil.getColumnName(field));
        desc.add(true);
        return this;
    }
}

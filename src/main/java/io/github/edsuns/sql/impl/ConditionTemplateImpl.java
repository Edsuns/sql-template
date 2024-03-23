package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.statement.ConditionTemplate;
import io.github.edsuns.sql.statement.OrderBy;
import io.github.edsuns.sql.statement.Where;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 10:47
 */
@ParametersAreNonnullByDefault
abstract class ConditionTemplateImpl<T extends Entity, Q extends Query> implements ConditionTemplate<T, Q> {

    protected final Class<T> entityClass;
    protected final Class<Q> queryClass;
    protected boolean whereSelective = false;
    protected WhereWrappedKeyword<T, Q, ?> where = null;
    protected Keyword<Q> groupBy = null;
    protected Keyword<Q> orderBy = null;
    protected Keyword<Q> limit = null;

    protected ConditionTemplateImpl(Class<T> entityClass, Class<Q> queryClass) {
        this.entityClass = entityClass;
        this.queryClass = queryClass;
    }

    @Override
    public ConditionTemplate<T, Q> whereSelective() {
        whereSelective = true;
        return this;
    }

    @Override
    public ConditionTemplate<T, Q> where(Consumer<Where<T, Q>> wheres) {
        if (where != null) {
            throw new IllegalStateException();
        }
        WhereInnerImpl<T, Q> inner = new WhereInnerImpl<>(new ArrayDeque<>());
        wheres.accept(inner);
        where = new WhereWrappedKeyword<>(inner, false, true, true);
        return this;
    }

    @Override
    public <X> ConditionTemplate<T, Q> groupBy(Collection<SerializableFunction<T, X>> columns, @Nullable Consumer<Where<T, Q>> having) {
        if (groupBy != null) {
            throw new IllegalStateException();
        }
        WhereInnerImpl<T, Q> inner = null;
        if (having != null) {
            inner = new WhereInnerImpl<>(new ArrayDeque<>());
            having.accept(inner);
        }
        groupBy = new GroupByKeyword<>(columns, inner);
        return this;
    }

    @Override
    public ConditionTemplate<T, Q> orderBy(Consumer<OrderBy<T>> orders) {
        if (orderBy != null) {
            throw new IllegalStateException();
        }
        OrderByKeyword<T, Q> orderBy = new OrderByKeyword<>(new ArrayDeque<>(), new ArrayDeque<>());
        orders.accept(orderBy);
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public <X extends Number> ConditionTemplate<T, Q> limit(SerializableFunction<Q, X> rows) {
        if (limit != null) {
            throw new IllegalStateException();
        }
        limit = new LimitKeyword<>(null, rows);
        return this;
    }

    @Override
    public <X extends Number> ConditionTemplate<T, Q> limit(SerializableFunction<Q, X> offset, SerializableFunction<Q, X> rows) {
        if (limit != null) {
            throw new IllegalStateException();
        }
        limit = new LimitKeyword<>(offset, rows);
        return this;
    }
}

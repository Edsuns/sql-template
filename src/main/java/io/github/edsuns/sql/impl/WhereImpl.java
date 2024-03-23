package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.statement.Where;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/05/31 15:38
 */
@ParametersAreNonnullByDefault
class WhereImpl<T extends Entity, Q extends Query> implements Where<T, Q> {

    protected final Queue<Keyword<Q>> keywords;

    public WhereImpl(Queue<Keyword<Q>> keywords) {
        this.keywords = keywords;
    }

    @Override
    public <X> Where<T, Q> equals(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        keywords.add(new WhereCompareKeyword<>(field, val, '='));
        return this;
    }

    @Override
    public <X> Where<T, Q> greater(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        keywords.add(new WhereCompareKeyword<>(field, val, '>'));
        return this;
    }

    @Override
    public <X> Where<T, Q> less(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        keywords.add(new WhereCompareKeyword<>(field, val, '<'));
        return this;
    }

    @Override
    public <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        return this.like(field, val, false);
    }

    @Override
    public <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean prefixMatching) {
        keywords.add(new WhereLikeKeyword<>(field, val, prefixMatching));
        return this;
    }

    @Override
    public <X> Where<T, Q> in(SerializableFunction<T, X> field, SerializableFunction<Q, List<X>> val) {
        keywords.add(new WhereInKeyword<>(field, val));
        return this;
    }

    @Override
    public Where<T, Q> or() {
        keywords.add(new WhereAndOrKeyword<>(false));
        return this;
    }

    @Override
    public Where<T, Q> or(Consumer<Where<T, Q>> wheres) {
        WhereInnerImpl<T, Q> inner = new WhereInnerImpl<>(new ArrayDeque<>());
        wheres.accept(inner);
        keywords.add(new WhereWrappedKeyword<>(inner, true, false, false));
        return this;
    }

    @Override
    public Where<T, Q> and() {
        keywords.add(new WhereAndOrKeyword<>(true));
        return this;
    }

    @Override
    public Where<T, Q> and(Consumer<Where<T, Q>> wheres) {
        WhereInnerImpl<T, Q> inner = new WhereInnerImpl<>(new ArrayDeque<>());
        wheres.accept(inner);
        keywords.add(new WhereWrappedKeyword<>(inner, true, false, true));
        return this;
    }

}

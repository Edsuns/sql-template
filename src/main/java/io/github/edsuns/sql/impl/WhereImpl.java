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
abstract class WhereImpl<T extends Entity, Q extends Query> implements Keyword<T, Q>, Where<T, Q> {

    protected final Queue<Keyword<T, Q>> keywords;

    WhereImpl(Queue<Keyword<T, Q>> keywords) {
        this.keywords = keywords;
    }

    @Override
    public <X> Where<T, Q> equals(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        return this.equals(field, val, false);
    }

    @Override
    public <X> Where<T, Q> equals(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective) {
        keywords.add(new WhereCompareKeyword<>(field, val, '=', selective));
        return this;
    }

    @Override
    public <X> Where<T, Q> greater(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        return this.greater(field, val, false);
    }

    @Override
    public <X> Where<T, Q> greater(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective) {
        keywords.add(new WhereCompareKeyword<>(field, val, '>', selective));
        return this;
    }

    @Override
    public <X> Where<T, Q> less(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        return this.less(field, val, false);
    }

    @Override
    public <X> Where<T, Q> less(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective) {
        keywords.add(new WhereCompareKeyword<>(field, val, '<', selective));
        return this;
    }

    @Override
    public <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val) {
        return this.like(field, val, false, false);
    }

    @Override
    public <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective) {
        return this.like(field, val, false, selective);
    }

    @Override
    public <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean prefixMatching, boolean selective) {
        keywords.add(new WhereLikeKeyword<>(field, val, prefixMatching, selective));
        return this;
    }

    @Override
    public <X> Where<T, Q> in(SerializableFunction<T, X> field, SerializableFunction<Q, List<X>> val) {
        return this.in(field, val, false);
    }

    @Override
    public <X> Where<T, Q> in(SerializableFunction<T, X> field, SerializableFunction<Q, List<X>> val, boolean selective) {
        keywords.add(new WhereInKeyword<>(field, val, selective));
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

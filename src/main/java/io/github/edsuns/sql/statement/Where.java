package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplateBuilder;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 16:32
 */
@ParametersAreNonnullByDefault
public interface Where<T extends Entity, Q extends Query> extends SqlTemplateBuilder<T, Q> {

    String KEY_WORD_WHERE = "WHERE";
    String KEY_WORD_AND = "AND";
    String KEY_WORD_OR = "OR";
    String KEY_WORD_IN = "IN";

    <X> Where<T, Q> equals(SerializableFunction<T, X> field, SerializableFunction<Q, X> val);

    <X> Where<T, Q> equals(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective);

    <X> Where<T, Q> greater(SerializableFunction<T, X> field, SerializableFunction<Q, X> val);

    <X> Where<T, Q> greater(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective);

    <X> Where<T, Q> less(SerializableFunction<T, X> field, SerializableFunction<Q, X> val);

    <X> Where<T, Q> less(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective);

    <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val);

    <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean selective);

    <X> Where<T, Q> like(SerializableFunction<T, X> field, SerializableFunction<Q, X> val, boolean prefixMatching, boolean selective);

    <X> Where<T, Q> in(SerializableFunction<T, X> field, SerializableFunction<Q, List<X>> val);

    <X> Where<T, Q> in(SerializableFunction<T, X> field, SerializableFunction<Q, List<X>> val, boolean selective);

    Where<T, Q> or();

    Where<T, Q> or(Consumer<Where<T, Q>> wheres);

    Where<T, Q> and();

    Where<T, Q> and(Consumer<Where<T, Q>> wheres);
}

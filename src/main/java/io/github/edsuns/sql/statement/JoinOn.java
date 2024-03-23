package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author edsuns@qq.com
 * @since 2023/04/04 13:49
 */
@ParametersAreNonnullByDefault
public interface JoinOn<T extends Entity, Q extends Query> extends Where<T, Q> {

    <A, B> JoinOn<T, Q> columnEquals(SerializableFunction<T, A> left, SerializableFunction<Q, B> right);

    <A, B> JoinOn<T, Q> columnEqualsAnother(SerializableFunction<T, A> left, SerializableFunction<T, B> right);
}

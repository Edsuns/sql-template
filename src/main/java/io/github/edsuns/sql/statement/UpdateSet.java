package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:07
 */
@ParametersAreNonnullByDefault
public interface UpdateSet<T extends Entity, Q extends Query> {
    String KEY_WORD = "SET";

    <X> UpdateSet<T, Q> assign(SerializableFunction<T, X> field, SerializableFunction<Q, X> val);
}

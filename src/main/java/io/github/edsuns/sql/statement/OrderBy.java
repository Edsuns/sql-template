package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 16:27
 */
@ParametersAreNonnullByDefault
public interface OrderBy<T extends Entity> {

    <X> OrderBy<T> asc(SerializableFunction<T, X> field);

    <X> OrderBy<T> desc(SerializableFunction<T, X> field);
}

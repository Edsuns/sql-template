package io.github.edsuns.sql.util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.function.Function;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:33
 */
@ParametersAreNonnullByDefault
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}

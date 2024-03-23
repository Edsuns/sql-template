package io.github.edsuns.sql.util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:34
 */
@ParametersAreNonnullByDefault
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {
}

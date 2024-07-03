package io.github.edsuns.sql.protocol;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 9:33
 */
@ParametersAreNonnullByDefault
public interface SqlTemplate<T extends Entity, Q extends Query, R> {

    String PLACEHOLDER = "?";
}

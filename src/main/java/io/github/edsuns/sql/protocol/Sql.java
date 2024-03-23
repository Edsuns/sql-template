package io.github.edsuns.sql.protocol;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/20 20:19
 */
@ParametersAreNonnullByDefault
public interface Sql {
    String getSqlTemplateString();

    Queue<Object> getVariables();
}

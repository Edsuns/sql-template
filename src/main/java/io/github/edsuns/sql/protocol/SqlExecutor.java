package io.github.edsuns.sql.protocol;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 12:41
 */
@ParametersAreNonnullByDefault
public interface SqlExecutor {
    /**
     * execute an select sql script
     *
     * @param sql         sql script
     * @param variables   variables
     * @param resultClass element type
     * @param <R>         any type
     * @return a collection of data
     */
    <R> List<R> read(String sql, Queue<Object> variables, Class<R> resultClass);

    /**
     * execute an update/insert sql script
     *
     * @param sql       sql script
     * @param variables variables
     * @return affected rows
     */
    long write(String sql, Queue<Object> variables);
}

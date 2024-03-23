package io.github.edsuns.sql.protocol;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/23 14:03
 */
public class DefaultSqlExecutor {

    public static SqlExecutor INSTANCE = new NoImpl();

    private static class NoImpl implements SqlExecutor {
        @Override
        public <R> List<R> read(String sql, Queue<Object> variables, Class<R> resultClass) { return Collections.emptyList(); }
        @Override
        public long write(String sql, Queue<Object> variables) { return 0L; }
    }
}

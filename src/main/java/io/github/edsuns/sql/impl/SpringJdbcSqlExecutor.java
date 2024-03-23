package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.SqlExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/23 11:08
 */
@ParametersAreNonnullByDefault
public class SpringJdbcSqlExecutor implements SqlExecutor {
    private final JdbcTemplate jdbcTemplate;

    public SpringJdbcSqlExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public <R> List<R> read(String sql, Queue<Object> variables, Class<R> resultClass) {
        return jdbcTemplate.queryForList(sql, resultClass, variables.toArray());
    }

    @Override
    public long write(String sql, Queue<Object> variables) {
        return jdbcTemplate.update(sql, variables.toArray());
    }
}

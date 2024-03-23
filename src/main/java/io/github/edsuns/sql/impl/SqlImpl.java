package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Sql;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 11:40
 */
@ParametersAreNonnullByDefault
class SqlImpl implements Sql {
    private final String sqlTemplate;
    private final Queue<Object> variables;

    public SqlImpl(String sqlTemplate, Queue<Object> variables) {
        this.sqlTemplate = sqlTemplate;
        this.variables = variables;
    }

    @Override
    public String getSqlTemplateString() {
        return sqlTemplate;
    }

    @Override
    public Queue<Object> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return "[" + sqlTemplate + "] " + variables;
    }
}

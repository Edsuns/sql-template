package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Queue;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 11:53
 */
@ParametersAreNonnullByDefault
class CountSqlTemplateImpl<T extends Entity, Q extends Query> extends SqlTemplateImpl<T, Q, Long> {
    public CountSqlTemplateImpl(Queue<Keyword<Q>> keywords) {
        super(keywords, Long.class);
    }
}

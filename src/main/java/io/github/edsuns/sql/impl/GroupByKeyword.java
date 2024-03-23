package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.github.edsuns.sql.statement.ConditionTemplate.KEY_WORD_GROUP_BY;
import static io.github.edsuns.sql.statement.ConditionTemplate.KEY_WORD_HAVING;
import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 18:12
 */
@ParametersAreNonnullByDefault
class GroupByKeyword<T, Q, X> implements Keyword<Q> {

    protected final Collection<SerializableFunction<T, X>> entityFields;
    @Nullable
    protected final Keyword<Q> having;

    public GroupByKeyword(Collection<SerializableFunction<T, X>> entityFields, @Nullable Keyword<Q> having) {
        this.entityFields = entityFields;
        this.having = having;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        appendSpaceIfNotPresent(sql);
        sql.append(KEY_WORD_GROUP_BY);
        sql.append(' ').append(entityFields.stream().map(SqlUtil::getColumnName).collect(Collectors.joining(",")));
        if (having != null) {
            int beforeIndex = sql.length();
            sql.append(' ').append(KEY_WORD_HAVING).append(' ');
            boolean affected = having.parseIntoSql(sql, variableConsumer, query);
            if (!affected) {
                if (beforeIndex < sql.length()) {
                    sql.delete(beforeIndex, sql.length());
                }
                return false;
            }
        }
        return true;
    }
}

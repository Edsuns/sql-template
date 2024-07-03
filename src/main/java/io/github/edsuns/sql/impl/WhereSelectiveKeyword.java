package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.github.edsuns.sql.statement.Where.KEY_WORD_AND;
import static io.github.edsuns.sql.util.SqlUtil.appendAndIfNotPresent;
import static io.github.edsuns.sql.util.SqlUtil.appendSpaceIfNotPresent;

/**
 * @author edsuns@qq.com
 * @since 2024/3/22 9:46
 */
@ParametersAreNonnullByDefault
class WhereSelectiveKeyword<T extends Entity, Q> implements Keyword<T, Q> {
    private final Map<String, Field> entityFields;
    private final Map<String, Field> queryFields;

    public WhereSelectiveKeyword(Collection<Field> entityFields, Collection<Field> queryFields) {
        this.entityFields = entityFields.stream().collect(Collectors.toMap(SqlUtil::getColumnName, x -> x, (a, b) -> a, TreeMap::new));
        this.queryFields = queryFields.stream().collect(Collectors.toMap(SqlUtil::getColumnName, x -> x, (a, b) -> a, TreeMap::new));
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        boolean open = appendAndIfNotPresent(sql);
        appendSpaceIfNotPresent(sql);
        if (open) {
            sql.append('(');
        }
        boolean affected = false;
        for (String columnName : entityFields.keySet()) {
            Field field = queryFields.get(columnName);
            if (field != null) {
                Object val = SqlUtil.value(query, field);
                if (val != null) {
                    appendSpaceIfNotPresent(sql);
                    if (affected) {
                        sql.append(KEY_WORD_AND);
                    }
                    appendSpaceIfNotPresent(sql);
                    sql.append(columnName).append('=').append(SqlTemplate.PLACEHOLDER);
                    variableConsumer.accept(val);
                    affected = true;
                }
            }
        }
        if (open) {
            // close
            sql.append(')');
        }
        return affected;
    }
}

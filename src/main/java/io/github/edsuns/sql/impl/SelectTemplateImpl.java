package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplate;
import io.github.edsuns.sql.protocol.SqlTemplateBuilder;
import io.github.edsuns.sql.statement.Join;
import io.github.edsuns.sql.statement.JoinOn;
import io.github.edsuns.sql.statement.SelectTemplate;
import io.github.edsuns.sql.util.SerializableFunction;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/04/04 15:03
 */
@ParametersAreNonnullByDefault
public class SelectTemplateImpl<T extends Entity, Q extends Query> extends ConditionTemplateImpl<T, Q> implements SelectTemplate<T, Q> {

    private String tableName = null;
    private Collection<String> columns = null;
    private boolean all;
    private boolean distinct;
    private boolean distinctrow;

    public SelectTemplateImpl(Class<T> entityClass, Class<Q> queryClass) {
        super(entityClass, queryClass);
    }

    @Override
    public <X> SelectTemplate<T, Q> select(Class<T> entity, @Nullable Collection<SerializableFunction<T, X>> columns) {
        this.tableName = SqlUtil.getTableName(entity);
        this.columns = columns == null ? SqlUtil.getColumnNames(entity) : SqlUtil.getColumnNames(columns);
        return this;
    }

    @Override
    public SelectTemplate<T, Q> all() {
        all = true;
        return this;
    }

    @Override
    public SelectTemplate<T, Q> distinct() {
        distinct = true;
        return this;
    }

    @Override
    public SelectTemplate<T, Q> distinctrow() {
        distinctrow = true;
        return this;
    }

    @Override
    public <X> Join<T, Q> leftJoin(Class<X> entity, Consumer<JoinOn<T, Q>> on) {
        return null;
    }

    @Override
    public <X> Join<T, Q> leftJoin(SqlTemplateBuilder<T, Q> entity, Consumer<JoinOn<T, Q>> on) {
        return null;
    }

    @Override
    public <X> Join<T, Q> rightJoin(Class<X> entity, Consumer<JoinOn<T, Q>> on) {
        return null;
    }

    @Override
    public <X> Join<T, Q> rightJoin(SqlTemplateBuilder<T, Q> entity, Consumer<JoinOn<T, Q>> on) {
        return null;
    }

    @Override
    public <X> Join<T, Q> innerJoin(Class<X> entity, Consumer<JoinOn<T, Q>> on) {
        return null;
    }

    @Override
    public <X> Join<T, Q> innerJoin(SqlTemplateBuilder<T, Q> entity, Consumer<JoinOn<T, Q>> on) {
        return null;
    }

    private Queue<Keyword<Q>> buildSql(boolean count) {
        if (tableName == null) {
            throw new IllegalStateException();
        }
        Queue<Keyword<Q>> keywords = new ArrayDeque<>();
        keywords.add(new SelectFromKeyword<>(tableName,
                count ? "COUNT(1)" : String.join(",", columns),
                all, distinct, distinctrow));
        if (where != null || whereSelective) {
            if (where == null) {
                where = new WhereWrappedKeyword<>(null, false, true, true);
            }
            if (whereSelective) {
                where.setWhereSelective(new WhereSelectiveKeyword<>(
                        SqlUtil.getNonStaticFields(entityClass),
                        SqlUtil.getNonStaticFields(queryClass)));
            }
            keywords.add(where);
        }
        if (groupBy != null) {
            keywords.add(groupBy);
        }
        if (orderBy != null) {
            keywords.add(orderBy);
        }
        if (limit != null) {
            keywords.add(limit);
        }
        return keywords;
    }

    @Override
    public SqlTemplate<T, Q, T> onlyOne() {
        return new SqlTemplateImpl<>(buildSql(false), entityClass);
    }

    @Override
    public SqlTemplate<T, Q, List<T>> list() {
        return new ListSqlTemplateImpl<>(buildSql(false), entityClass);
    }

    @Override
    public SqlTemplate<T, Q, Long> count() {
        return new CountSqlTemplateImpl<>(buildSql(true));
    }
}

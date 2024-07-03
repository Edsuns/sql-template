package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.WriteStatement;
import io.github.edsuns.sql.statement.UpdateSet;
import io.github.edsuns.sql.statement.UpdateTemplate;
import io.github.edsuns.sql.util.SqlUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/04/04 15:04
 */
@ParametersAreNonnullByDefault
public class UpdateTemplateImpl<T extends Entity, Q extends Query> extends ConditionTemplateImpl<T, Q> implements UpdateTemplate<T, Q> {

    private String tableName = null;
    private boolean lowPriority = false;
    private boolean ignore = false;
    private UpdateSetKeyword<T, Q> setSql = null;

    public UpdateTemplateImpl(Class<T> entityClass, Class<Q> queryClass) {
        super(entityClass, queryClass);
    }

    @Override
    public UpdateTemplate<T, Q> update(Class<T> entity) {
        tableName = SqlUtil.getTableName(entity);
        return this;
    }

    @Override
    public UpdateTemplate<T, Q> set(Consumer<UpdateSet<T, Q>> setters) {
        setters.accept(makeSetSql());
        return this;
    }

    @Override
    public UpdateTemplate<T, Q> setSelective() {
        makeSetSql().setSelective(true, SqlUtil.getNonStaticFields(entityClass));
        return this;
    }

    private UpdateSetKeyword<T, Q> makeSetSql() {
        if (this.setSql == null) {
            this.setSql = new UpdateSetKeyword<>(new ArrayDeque<>(), new ArrayDeque<>());
        }
        return this.setSql;
    }

    @Override
    public UpdateTemplate<T, Q> ignore() {
        ignore = true;
        return this;
    }

    @Override
    public UpdateTemplate<T, Q> lowPriority() {
        lowPriority = true;
        return this;
    }

    private Queue<Keyword<T, Q>> buildSql() {
        if (tableName == null) {
            throw new IllegalStateException();
        }
        Queue<Keyword<T, Q>> keywords = new ArrayDeque<>();
        keywords.add(new UpdateKeyword<>(tableName, lowPriority, ignore));
        if (setSql == null) {
            throw new IllegalStateException();
        }
        keywords.add(setSql);
        if (where != null) {
            keywords.add(where);
        }
        return keywords;
    }

    @Override
    public WriteStatement<T, Q, Long> affected() {
        return new AffectedSqlTemplateImpl<>(buildSql());
    }
}

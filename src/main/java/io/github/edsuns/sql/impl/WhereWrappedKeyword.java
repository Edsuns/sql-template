package io.github.edsuns.sql.impl;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.statement.Where;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static io.github.edsuns.sql.util.SqlUtil.*;

/**
 * @author edsuns@qq.com
 * @since 2024/3/21 14:52
 */
@ParametersAreNonnullByDefault
class WhereWrappedKeyword<T extends Entity, Q, X> implements Keyword<T, Q> {
    @Nullable
    protected final Keyword<T, Q> inner;
    @Nullable
    protected Keyword<T, Q> whereSelective;
    protected final boolean requireBraces;
    protected final boolean appendWhere;
    protected final boolean andOr;

    protected WhereWrappedKeyword(@Nullable Keyword<T, Q> inner, boolean requireBraces, boolean appendWhere, boolean andOr) {
        this.inner = inner;
        this.requireBraces = requireBraces;
        this.appendWhere = appendWhere;
        this.andOr = andOr;
    }

    public void setWhereSelective(Keyword<T, Q> whereSelective) {
        this.whereSelective = whereSelective;
    }

    @Override
    public boolean parseIntoSql(StringBuilder sql, Consumer<Object> variableConsumer, @Nullable Q query) {
        if (query == null) {
            return false;
        }
        RollbackKeyWordAppender<T, Q> appender = new RollbackKeyWordAppender<>(sql);

        appendSpaceIfNotPresent(sql);
        if (appendWhere) {
            sql.append(Where.KEY_WORD_WHERE).append(' ');
        }

        appender.mark(1);
        if (andOr) {
            appendAndIfNotPresent(sql);
        } else {
            appendOrIfNotPresent(sql);
        }
        int openIndex = sql.length();
        sql.append('(');
        boolean innerAffected = inner != null && appender.append(inner, null, query, variableConsumer);
        int closeIndex = sql.length();
        sql.append(')');
        if (!innerAffected) {
            appender.rollback(1);
        }

        appender.mark(2);
        boolean whereSelectiveAffected = whereSelective != null && appender.append(whereSelective, null, query, variableConsumer);
        if (!whereSelectiveAffected) {
            appender.rollback(2);
            if (innerAffected && !requireBraces) {
                sql.deleteCharAt(openIndex);
                sql.deleteCharAt(closeIndex - 1);
            }
        }

        boolean anyAffected = innerAffected || whereSelectiveAffected;
        if (!anyAffected) {
            appender.rollback();
        }
        return anyAffected;
    }

}

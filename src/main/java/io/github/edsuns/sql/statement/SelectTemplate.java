package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplateBuilder;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

/**
 * <a href="https://dev.mysql.com/doc/refman/8.0/en/select.html">SELECT Statement - MySQL Reference Manual</a>
 *
 * @author edsuns@qq.com
 * @since 2023/04/04 13:41
 */
@ParametersAreNonnullByDefault
public interface SelectTemplate<T extends Entity, Q extends Query> extends SqlTemplateBuilder<T, Q>, ConditionTemplate<T, Q>, Join<T, Q> {

    String KEY_WORD_SELECT = "SELECT";
    String KEY_WORD_FROM = "FROM";
    String KEY_WORD_ALL = "ALL";
    String KEY_WORD_DISTINCT = "DISTINCT";
    String KEY_WORD_DISTINCTROW = "DISTINCTROW";

    <X> SelectTemplate<T, Q> select(Class<T> entity, @Nullable Collection<SerializableFunction<T, X>> columns);

    SelectTemplate<T, Q> all();

    SelectTemplate<T, Q> distinct();

    SelectTemplate<T, Q> distinctrow();

}

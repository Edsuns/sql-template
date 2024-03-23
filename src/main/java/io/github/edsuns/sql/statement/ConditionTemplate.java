package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplateBuilder;
import io.github.edsuns.sql.util.SerializableFunction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:41
 */
@ParametersAreNonnullByDefault
public interface ConditionTemplate<T extends Entity, Q extends Query> extends SqlTemplateBuilder<T, Q> {
    String KEY_WORD_GROUP_BY = "GROUP BY";
    String KEY_WORD_HAVING = "HAVING";

    ConditionTemplate<T, Q> whereSelective();

    ConditionTemplate<T, Q> where(Consumer<Where<T, Q>> wheres);

    <X> ConditionTemplate<T, Q> groupBy(Collection<SerializableFunction<T, X>> columns, @Nullable Consumer<Where<T, Q>> having);

    ConditionTemplate<T, Q> orderBy(Consumer<OrderBy<T>> orders);

    <X extends Number> ConditionTemplate<T, Q> limit(SerializableFunction<Q, X> rows);

    <X extends Number> ConditionTemplate<T, Q> limit(SerializableFunction<Q, X> offset, SerializableFunction<Q, X> rows);
}

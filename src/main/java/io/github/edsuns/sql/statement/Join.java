package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplateBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author edsuns@qq.com
 * @since 2023/04/04 13:48
 */
@ParametersAreNonnullByDefault
public interface Join<T extends Entity, Q extends Query> extends SqlTemplateBuilder<T, Q>, ConditionTemplate<T, Q> {

    <X> Join<T, Q> leftJoin(Class<X> entity, Consumer<JoinOn<T, Q>> on);

    <X> Join<T, Q> leftJoin(SqlTemplateBuilder<T, Q> entity, Consumer<JoinOn<T, Q>> on);

    <X> Join<T, Q> rightJoin(Class<X> entity, Consumer<JoinOn<T, Q>> on);

    <X> Join<T, Q> rightJoin(SqlTemplateBuilder<T, Q> entity, Consumer<JoinOn<T, Q>> on);

    <X> Join<T, Q> innerJoin(Class<X> entity, Consumer<JoinOn<T, Q>> on);

    <X> Join<T, Q> innerJoin(SqlTemplateBuilder<T, Q> entity, Consumer<JoinOn<T, Q>> on);
}

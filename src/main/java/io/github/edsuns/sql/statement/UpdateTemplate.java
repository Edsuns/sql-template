package io.github.edsuns.sql.statement;

import io.github.edsuns.sql.protocol.Entity;
import io.github.edsuns.sql.protocol.Query;
import io.github.edsuns.sql.protocol.SqlTemplateBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * <a href="https://dev.mysql.com/doc/refman/8.0/en/update.html">UPDATE Statement - MySQL Reference Manual</a>
 *
 * @author edsuns@qq.com
 * @since 2023/03/31 15:39
 */
@ParametersAreNonnullByDefault
public interface UpdateTemplate<T extends Entity, Q extends Query> extends SqlTemplateBuilder<T, Q>, ConditionTemplate<T, Q> {

    String KEY_WORD_UPDATE = "UPDATE";
    String KEY_WORD_IGNORE = "IGNORE";
    String KEY_WORD_LOW_PRIORITY = "LOW_PRIORITY";

    UpdateTemplate<T, Q> update(Class<T> entity);

    <X> UpdateTemplate<T, Q> set(Consumer<UpdateSet<T, Q>> setters);

    UpdateTemplate<T, Q> ignore();

    UpdateTemplate<T, Q> lowPriority();

}

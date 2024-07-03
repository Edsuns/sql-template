package io.github.edsuns.sql.protocol;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:30
 */
@ParametersAreNonnullByDefault
public interface SqlTemplateBuilder<T extends Entity, Q extends Query> {

    default ReadStatement<T, Q, T> onlyOne() {
        // only implemented by SelectTemplate
        throw new UnsupportedOperationException();
    }

    default ReadStatement<T, Q, List<T>> list() {
        // only implemented by SelectTemplate
        throw new UnsupportedOperationException();
    }

    default ReadStatement<T, Q, Long> count() {
        // only implemented by SelectTemplate
        throw new UnsupportedOperationException();
    }

    default WriteStatement<T, Q, Long> affected() {
        // only implemented by UpdateTemplate
        throw new UnsupportedOperationException();
    }
}

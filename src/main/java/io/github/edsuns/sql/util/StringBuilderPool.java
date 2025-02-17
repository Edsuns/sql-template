package io.github.edsuns.sql.util;

/**
 * @author edsuns@qq.com
 * @since 2024/3/23 10:49
 */
public final class StringBuilderPool {
    private static int INITIAL_CAPACITY = 512;
    private static final ThreadLocal<StringBuilder> THREAD_STRING_BUILDER =
            ThreadLocal.withInitial(() -> new StringBuilder(INITIAL_CAPACITY));

    public static StringBuilder get() {
        StringBuilder stringBuilder = THREAD_STRING_BUILDER.get();
        stringBuilder.setLength(0);
        // TODO capacity contraction
        return stringBuilder;
    }

    public static void setInitialCapacity(int capacity) {
        INITIAL_CAPACITY = capacity;
    }
}

package io.github.edsuns.sql.util;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;
import java.util.function.Function;

/**
 * @author edsuns@qq.com
 * @since 2023/04/01 11:04
 */
@ParametersAreNonnullByDefault
public interface Cache<K, V> {

    /**
     * @param key   key
     * @param value that will be associated with {@param key}
     * @param ttl   time to live
     */
    void put(K key, @Nullable V value, Duration ttl);

    V computeIfAbsent(K key, Function<? super K, ? extends V> value, Duration ttl);

    /**
     * @param key key
     * @return value associated with {@param key}
     */
    @Nullable
    V get(K key);

    /**
     * @param key key
     * @return value associated with {@param key}
     */
    @Nullable
    V evict(K key);
}

package io.github.edsuns.sql.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author edsuns@qq.com
 * @since 2023/04/03 09:26
 */
class ConcurrentCacheTest {

    @Test
    void put() throws InterruptedException {
        ConcurrentCache<String, Integer> cache = new ConcurrentCache<>();
        cache.put("a", 1, Duration.ofMillis(100L));
        Thread.sleep(110L);
        assertNull(cache.get("a"));
    }
}
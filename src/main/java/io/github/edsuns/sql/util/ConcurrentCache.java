package io.github.edsuns.sql.util;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2023/04/01 10:06
 */
@ParametersAreNonnullByDefault
public class ConcurrentCache<K, V> implements Cache<K, V> {

    private static class Wrapper<V> {
        @Nullable
        private V value;
        private long expireAtMillis;

        @Nullable
        public V getValue() {
            return value;
        }

        public void setValue(@Nullable V value) {
            this.value = value;
        }

        public long getExpireAtMillis() {
            return expireAtMillis;
        }

        public void setExpireAtMillis(long expireAtMillis) {
            this.expireAtMillis = expireAtMillis;
        }

        public void recycle() {
            setValue(null);
            setExpireAtMillis(0L);
        }

    }

    private static class ObjPool<T> {
        private static final int MAX_QUEUE_SIZE = 16;
        private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

        T getOrCreate(Supplier<T> constructor) {
            T v = queue.poll();
            return v != null ? v : constructor.get();
        }

        void recycle(T value) {
            if (queue.size() < MAX_QUEUE_SIZE) {
                queue.add(value);
            }
        }
    }

    private final ConcurrentMap<K, Wrapper<V>> map;
    private final ConcurrentNavigableMap<Long, Set<K>> pMap;
    private final ObjPool<Wrapper<V>> objPool;

    public ConcurrentCache() {
        this.map = new ConcurrentHashMap<>();
        this.pMap = new ConcurrentSkipListMap<>();
        this.objPool = new ObjPool<>();
    }

    @Override
    public void put(K key, @Nullable V value, Duration ttl) {
        final long now = evictNow();
        final long expireAt = now + ttl.toMillis();
        Wrapper<V> w = objPool.getOrCreate(Wrapper::new);
        w.setValue(value);
        w.setExpireAtMillis(expireAt);
        map.compute(key, (k, v) -> {
            computeExpiration(expireAt, key);
            return w;
        });
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> value, Duration ttl) {
        final long now = evictNow();
        final long expireAt = now + ttl.toMillis();
        Wrapper<V> w = objPool.getOrCreate(Wrapper::new);
        w.setExpireAtMillis(expireAt);
        map.computeIfAbsent(key, k -> {
            w.setValue(requireNonNull(value.apply(key)));
            computeExpiration(expireAt, key);
            return w;
        });
        return w.getValue();
    }

    private void computeExpiration(long expireAt, K key) {
        pMap.compute(expireAt, (t, set) -> {
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(key);
            return set;
        });
    }

    @Nullable
    @Override
    public V get(K key) {
        evictNow();
        Wrapper<V> w = map.get(key);
        return w != null ? w.getValue() : null;
    }

    @Override
    public V evict(K key) {
        Wrapper<V> w = map.remove(key);
        if (w == null) {
            return null;
        }
        V v = w.getValue();

        pMap.computeIfPresent(w.getExpireAtMillis(), (ttl, k) -> {
            k.remove(key);
            return k;
        });

        // recycle
        w.recycle();
        objPool.recycle(w);

        return v;
    }

    private long evictNow() {
        // eviction
        long now = System.currentTimeMillis();
        Map.Entry<Long, Set<K>> first = pMap.firstEntry();
        if (first != null) {
            if (first.getKey() < now) {
                for (K x : first.getValue()) {
                    Wrapper<V> w = map.remove(x);
                    if (w != null) {
                        w.recycle();
                        objPool.recycle(w);
                    }
                }
                pMap.remove(first.getKey());
            }
        }
        return now;
    }
}

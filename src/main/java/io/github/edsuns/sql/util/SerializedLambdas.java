package io.github.edsuns.sql.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;

import static java.util.Objects.requireNonNull;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:32
 */
public final class SerializedLambdas {
    private SerializedLambdas() {
        // private
    }

    private static Duration LAMBDA_CACHE_ALIVE = Duration.ofHours(1L);
    public static void setLambdaCacheAlive(Duration lambdaCacheAlive) { LAMBDA_CACHE_ALIVE = requireNonNull(lambdaCacheAlive); }

    private static final Cache<SerializableFunction<?, ?>, java.lang.invoke.SerializedLambda> LAMBDA_CACHE = new ConcurrentCache<>();

    public static <T, R> java.lang.invoke.SerializedLambda getLambdaCached(SerializableFunction<T, R> lambda) {
        return LAMBDA_CACHE.computeIfAbsent(lambda, SerializedLambdas::getSerializedLambda, LAMBDA_CACHE_ALIVE);
    }

    public static <T, R> java.lang.invoke.SerializedLambda getLambda(SerializableFunction<T, R> lambda) {
        return getSerializedLambda(lambda);
    }

    public static <T> java.lang.invoke.SerializedLambda getLambda(SerializableConsumer<T> lambda) {
        return getSerializedLambda(lambda);
    }

    public static java.lang.invoke.SerializedLambda getSerializedLambda(Serializable lambda) {
        if (!lambda.getClass().isSynthetic()) {
            throw new IllegalArgumentException("require synthetic");
        }
        try {
            Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (java.lang.invoke.SerializedLambda) writeReplace.invoke(lambda);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return SerializedLambda.getSerializedLambda(lambda);
        }
    }
}

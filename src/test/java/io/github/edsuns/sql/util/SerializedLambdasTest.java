package io.github.edsuns.sql.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:35
 */
class SerializedLambdasTest {

    @Test
    void test() {
        java.lang.invoke.SerializedLambda lambda = SerializedLambdas.getLambda(Object::toString);
        assertEquals(Object.class.getName(), lambda.getImplClass().replace('/', '.'));
        assertEquals("toString", lambda.getImplMethodName());

        java.lang.invoke.SerializedLambda bySerialize = SerializedLambda.getSerializedLambda(
                (SerializableFunction<Object, String>) Object::toString);
        assertEquals("toString", bySerialize.getImplMethodName());

        SerializableConsumer<?> println = System.out::println;
        assertEquals("println", SerializedLambdas.getLambda(println).getImplMethodName());
    }
}
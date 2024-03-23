package io.github.edsuns.sql.util;

import java.io.*;

/**
 * @author edsuns@qq.com
 * @since 2023/03/31 15:33
 */
@SuppressWarnings("unused")
final class SerializedLambda implements Serializable {
    private static final long serialVersionUID = 8025925345765570181L;
    private Class<?> capturingClass;
    private String functionalInterfaceClass;
    private String functionalInterfaceMethodName;
    private String functionalInterfaceMethodSignature;
    private String implClass;
    private String implMethodName;
    private String implMethodSignature;
    private int implMethodKind;
    private String instantiatedMethodType;
    private Object[] capturedArgs;

    public static java.lang.invoke.SerializedLambda getSerializedLambda(Serializable lambda) {
        try {
            SerializedLambda l = (SerializedLambda) deserialize(serialize(lambda));
            return new java.lang.invoke.SerializedLambda(
                    l.capturingClass,
                    l.functionalInterfaceClass,
                    l.functionalInterfaceMethodName,
                    l.functionalInterfaceMethodSignature,
                    l.implMethodKind,
                    l.implClass,
                    l.implMethodName,
                    l.implMethodSignature,
                    l.instantiatedMethodType,
                    l.capturedArgs
            );
        } catch (IOException | ClassNotFoundException ex) {
            throw new InternalError(ex);
        }
    }

    private static byte[] serialize(Serializable obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(obj);
        return out.toByteArray();
    }

    private static Serializable deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                Class<?> clz = super.resolveClass(desc);
                return clz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clz;
            }
        };
        return (Serializable) in.readObject();
    }
}

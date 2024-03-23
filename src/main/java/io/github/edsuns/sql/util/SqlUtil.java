package io.github.edsuns.sql.util;

import io.github.edsuns.sql.statement.ConditionTemplate;
import io.github.edsuns.sql.statement.Where;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author edsuns@qq.com
 * @since 2023/05/31 16:11
 */
@ParametersAreNonnullByDefault
public final class SqlUtil {

    public static String quote(String name) {
        return "`" + name + "`";
    }

    public static <T> String getTableName(Class<T> clz) {
        return quote(camelToUnderline(clz.getSimpleName()));
    }

    static String camelToUnderline(String name) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (b.length() > 0) {
                    b.append('_');
                }
                b.append(Character.toLowerCase(c));
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

    public static <T> Collection<String> getColumnNames(Class<T> entity) {
        List<Field> fields = getNonStaticFields(entity);
        return fields.stream().map(SqlUtil::getColumnName).collect(Collectors.toList());
    }

    public static <T, X> Collection<String> getColumnNames(Collection<SerializableFunction<T, X>> columns) {
        return columns.stream().map(SqlUtil::getColumnName).collect(Collectors.toList());
    }

    public static String getColumnName(Field field) {
        return quote(camelToUnderline(field.getName()));
    }

    public static <T, X> String getColumnName(SerializableFunction<T, X> field) {
        SerializedLambda lambda = SerializedLambdas.getLambdaCached(field);
        String methodName = lambda.getImplMethodName();
        int start;
        if (methodName.startsWith("set") || methodName.startsWith("get")) {
            start = 3;
        } else if (methodName.startsWith("is")) {
            start = 2;
        } else {
            throw new IllegalArgumentException("illegal method name: " + methodName);
        }
        return quote(camelToUnderline(methodName.substring(start)));
    }

    public static void appendSpaceIfNotPresent(StringBuilder sql) {
        if (sql.length() > 0) {
            char lastChar = sql.charAt(sql.length() - 1);
            if (!Character.isWhitespace(lastChar) && lastChar != '(') {
                sql.append(' ');
            }
        }
    }

    public static boolean appendAndIfNotPresent(StringBuilder sql) {
        return appendIfNotPresent(sql, Where.KEY_WORD_AND, Where.KEY_WORD_OR,
                "(", Where.KEY_WORD_WHERE, ConditionTemplate.KEY_WORD_HAVING);
    }

    public static boolean appendOrIfNotPresent(StringBuilder sql) {
        return appendIfNotPresent(sql, Where.KEY_WORD_OR, Where.KEY_WORD_AND,
                "(", Where.KEY_WORD_WHERE, ConditionTemplate.KEY_WORD_HAVING);
    }

    public static boolean appendIfNotPresent(StringBuilder sql, String x,
                                             @Nullable String mutex1, @Nullable String mutex2,
                                             @Nullable String mutex3, @Nullable String mutex4) {
        int r = sql.length() - 1;
        while (r >= 0 && Character.isWhitespace(sql.charAt(r))) {
            r--;
        }
        int l = r;
        if (l > 0) {
            while (l >= 0 && !Character.isWhitespace(sql.charAt(l))) {
                l--;
            }
            l++;

            if (substringNotEquals(sql, l, r - l + 1, x)
                    && (mutex1 == null || substringNotEndsWith(sql, l, r - l + 1, mutex1))
                    && (mutex2 == null || substringNotEndsWith(sql, l, r - l + 1, mutex2))
                    && (mutex3 == null || substringNotEndsWith(sql, l, r - l + 1, mutex3))
                    && (mutex4 == null || substringNotEndsWith(sql, l, r - l + 1, mutex4))) {
                appendSpaceIfNotPresent(sql);
                sql.append(x).append(' ');
                return true;
            }
        }
        return false;
    }

    private static boolean substringNotEquals(CharSequence a, int aOffset, int count, CharSequence b) {
        if (aOffset + count > a.length() || count > b.length()) {
            return true;
        }
        for (int i = 0; i < count; i++) {
            if (a.charAt(aOffset + i) != b.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean substringNotEndsWith(CharSequence a, int aOffset, int count, CharSequence b) {
        int end = aOffset + count;
        if (end > a.length() || b.length() > count) {
            return true;
        }
        for (int i = b.length() - 1; i >= 0; i--) {
            if (a.charAt(end - b.length() + i) != b.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    public static List<Field> getNonStaticFields(Class<?> klass) {
        return getFields(klass, f -> !Modifier.isStatic(f.getModifiers()));
    }

    public static List<Field> getFields(Class<?> klass, Predicate<Field> filter) {
        List<Field> fields = new ArrayList<>();
        do {
            for (Field f : klass.getDeclaredFields()) {
                if (filter.test(f)) {
                    fields.add(f);
                }
            }
        } while ((klass = klass.getSuperclass()) != null);
        return fields;
    }

    /**
     * Get the object field value.
     *
     * @param o object to get field value from
     * @param f field descriptor
     * @return value, maybe a boxed primitive
     */
    public static Object value(Object o, Field f) {
        // Try 1. Get with Reflection:
        try {
            return f.get(o);
        } catch (Exception e) {
            // fall-through
        }

        // Try 2. Get with Reflection and setAccessible:
        try {
            f.setAccessible(true);
            return f.get(o);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

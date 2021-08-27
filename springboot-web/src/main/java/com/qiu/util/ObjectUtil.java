package com.qiu.util;

import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

public class ObjectUtil {

    private static Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    private static final int STACK_OFFSET = 2;

    private ObjectUtil() {

    }

    /**
     * 转换成需要的类型,转换失败返回空
     *
     * @param value 要转换的参数
     * @param clazz 目标类型
     * @return Returns null if the conversion failure
     */
    public static <T> T convert(Object value, Class<T> clazz) {
        return convertOrGet(value, null, clazz);
    }

    /**
     * 转换成需要的类型,转换失败返回默认值
     *
     * @param value      要转换的参数
     * @param defaultVal 默认值
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertOrGet(Object value, T defaultVal) {
        if (value == null) {
            return defaultVal;
        }
        Objects.requireNonNull(defaultVal, "parameters(defaultVal) cannot be null");
        Class<T> clazz = null;
        try {
            clazz = (Class<T>) defaultVal.getClass();
        } catch (Exception e) {
            return defaultVal;
        }
        return convertOrGet(value, defaultVal, clazz);
    }

    /**
     * 转换成需要的类型,转换失败返回默认值
     *
     * @param value      要转换的参数
     * @param defaultVal 默认值
     * @param clazz      目标类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertOrGet(Object value, T defaultVal, Class<T> clazz) {
        Objects.requireNonNull(clazz, "parameters(clazz) cannot be null");
        if (value == null) {
            return defaultVal;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        try {
            if (clazz == String.class) {
                return (T) value.toString();
            }
            if (clazz == Byte.class || clazz == byte.class) {
                if (value instanceof Number) {
                    return (T) Byte.valueOf(((Number) value).byteValue());
                } else if (value instanceof String && !((String) value).isEmpty() && isNumeric((String) value)) {
                    return (T) Byte.valueOf((String) value);
                }
                return defaultVal;
            }
            if (clazz == Character.class || clazz == char.class) {
                String str = value.toString();
                if (str.length() == 1) {
                    return (T) Character.valueOf(str.charAt(0));
                }
                return defaultVal;
            }
            if (clazz == Boolean.class || clazz == boolean.class) {
                if (value instanceof Number) {
                    return (T) Boolean.valueOf((((Number) value).intValue() > 0));
                } else if (value instanceof String && !((String) value).isEmpty()) {
                    if (isNumeric((String) value)) {
                        return (T) Boolean.valueOf(Integer.valueOf((String) value) > 0);
                    }
                    return (T) Boolean.valueOf(((String) value).toLowerCase().charAt(0) == 't');
                }
                return defaultVal;
            }
            if (clazz == Short.class || clazz == short.class) {
                if (value instanceof Number) {
                    return (T) Short.valueOf(((Number) value).shortValue());
                } else if (value instanceof String && !((String) value).isEmpty() && isNumeric((String) value)) {
                    return (T) Short.valueOf((String) value);
                }
                return defaultVal;
            }
            if (clazz == Integer.class || clazz == int.class) {
                if (value instanceof Number) {
                    return (T) Integer.valueOf(((Number) value).intValue());
                } else if (value instanceof String && !((String) value).isEmpty() && isNumeric((String) value)) {
                    return (T) Integer.valueOf((String) value);
                }
                return defaultVal;
            }
            if (clazz == Long.class || clazz == long.class) {
                if (value instanceof Number) {
                    return (T) Long.valueOf(((Number) value).longValue());
                } else if (value instanceof String && !((String) value).isEmpty() && isNumeric((String) value)) {
                    return (T) Long.valueOf((String) value);
                }
                return defaultVal;
            }
            if (clazz == Float.class || clazz == float.class) {
                if (value instanceof Number) {
                    return (T) Float.valueOf(((Number) value).floatValue());
                } else if (value instanceof String && !((String) value).isEmpty()) {
                    return (T) Float.valueOf((String) value);
                }
                return defaultVal;
            }
            if (clazz == Double.class || clazz == double.class) {
                if (value instanceof Number) {
                    return (T) Double.valueOf(((Number) value).doubleValue());
                } else if (value instanceof String && !((String) value).isEmpty()) {
                    return (T) Double.valueOf((String) value);
                }
                return defaultVal;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return defaultVal;
    }

    /**
     * 判断字符串是否由数字组成
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (i == 0 && str.charAt(0) == '-') {
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<String> trimStrList(List<String> list) {
        if (list == null) {
            return null;
        }
        String tmp = null;
        for (Iterator<String> ite = list.iterator(); ite.hasNext(); ) {
            if ((tmp = ite.next()) == null || tmp.isEmpty()) {
                ite.remove();
            }
        }
        return list;
    }

    public static <T> List<T> trimList(List<T> list) {
        if (list == null) {
            return null;
        }
        for (Iterator<T> ite = list.iterator(); ite.hasNext(); ) {
            if ((ite.next()) == null) {
                ite.remove();
            }
        }
        return list;
    }

    public static <T, U> U getFromMap(Map<T, U> map, T key, Supplier<U> supplier) {
        U o = null;
        if ((o = map.get(key)) == null) {
            o = supplier.get();
            map.put(key, o);
        }
        return o;
    }

    @SafeVarargs
    public static <T> List<Object> asObjectList(T... a) {
        if (a == null) {
            return null;
        }
        List<Object> list = new ArrayList<>(a.length);
        for (Object o : a) {
            list.add(o);
        }
        return list;
    }

    public static <T> List<Object> asObjectList(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        List<Object> list = new ArrayList<>(coll.size());
        for (Object o : coll) {
            list.add(o);
        }
        return list;
    }

    /**
     * 将list分割成多个更小的list，返回的集合不可增删
     *
     * @param list
     * @param num
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int num) {
        if (list == null || list.isEmpty() || num <= 0) {
            return Collections.emptyList();
        }

        if (list.size() <= num) {
            List<List<T>> result = new ArrayList<List<T>>(1);
            result.add(list);
            return result;
        }

        int size = list.size() % num == 0 ? (list.size() / num) : (list.size() / num + 1);

        List<List<T>> result = new ArrayList<List<T>>(size);

        List<T> subList = null;
        for (T obj : list) {
            if (subList == null) {
                subList = new ArrayList<T>(num);
            }
            subList.add(obj);
            if (subList.size() >= num) {
                result.add(subList);
                subList = null;
            }
        }
        if (subList != null) {
            result.add(subList);
        }

        return result;
    }

    public static <K, V> List<Map<K, V>> splitMap(Map<K, V> map, int num) {
        if (map == null || map.isEmpty() || num <= 0) {
            return Collections.emptyList();
        }

        if (map.size() <= num) {
            List<Map<K, V>> result = new ArrayList<>(1);
            result.add(map);
            return result;
        }

        int size = map.size() % num == 0 ? (map.size() / num) : (map.size() / num + 1);

        List<Map<K, V>> result = new ArrayList<>(size);

        Map<K, V> subMap = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (subMap == null) {
                subMap = new HashMap<>(num + 1, 1f);
            }
            subMap.put(entry.getKey(), entry.getValue());
            if (subMap.size() >= num) {
                result.add(subMap);
                subMap = null;
            }
        }
        if (subMap != null) {
            result.add(subMap);
        }

        return result;
    }

    public static <T> List<T> getSubList(List<T> customerList, int skip, int size) {
        if (skip >= customerList.size()) {
            return Collections.emptyList();
        }
        int end = Math.min(skip + size, customerList.size());
        return customerList.subList(skip, end);
    }

    public static <T> T notNull(T t, T def) {
        return t == null ? def : t;
    }

    public static String notEmpty(String t, String def) {
        return StringUtil.isEmpty(t) ? def : t;
    }

    public static <T extends Number> T ZeroToNull(T number) {
        return number == null || number.intValue() == 0 ? null : number;
    }

    public static String EmptyToNull(String t) {
        return StringUtil.isEmpty(t) ? null : t;
    }

    /**
     * 将浮点数转为字符串
     *
     * @param value
     * @param numberPrecision 小数点后保留几位
     * @return
     */
    public static String floatToString(double value, int numberPrecision) {
        try {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                logger.error("double value is Nan/Infinite", new Exception());
                value = 0;
            }
            return BigDecimal.valueOf(value).setScale(numberPrecision, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            logger.error(value + ", " + e.getMessage(), e);
            throw e;
        }
    }

    static final        String   THIS_CLASS_NAME     = ObjectUtil.class.getName();
    public static final String[] ONLY_WIS_CODE_STACK = new String[]{"com.bj58.spat.wis"};

    public static String getMethodStack(int depth) {
        return getMethodStack(++depth, 1);
    }

    public static String getMethodStack(int depth, int skip) {
        return getMethodStack(depth, ++skip, null, null);
    }

    public static String getMethodStack(int depth, int skip, String[] exclusionClassPathPrefix,
                                        String[] inclusionClassPathPrefix) {
        try {
            return printMethodStack(depth, skip + STACK_OFFSET, exclusionClassPathPrefix, inclusionClassPathPrefix,
                    Thread.currentThread().getStackTrace());
        } catch (Exception e) {
            return "";
        }
    }

    public static String printMethodStack(int depth, StackTraceElement[] ste) {
        try {
            return printMethodStack(depth, 0, null, null, ste);
        } catch (Exception e) {
            return "";
        }
    }

    public static String printMethodStack(int depth, int skip, String[] exclusionClassPathPrefix,
                                          String[] inclusionClassPathPrefix, StackTraceElement[] ste) {
        StringBuilder sb = new StringBuilder(128);
        depth = Math.min(depth, 10);
        skip = Math.max(skip, 0);
        try {
            Stack<String> stack = new Stack<>();
            int index = 0;
            StackTraceElement element = null;
            String cName = null;
            outer:
            for (int i = 0; i < depth && (index = skip + i) < ste.length; i++) {
                element = ste[index];
                cName = element.getClassName();
                if (THIS_CLASS_NAME.equals(cName)) {
                    continue;
                }
                if (exclusionClassPathPrefix != null && exclusionClassPathPrefix.length > 0) {
                    for (String classPathPrefix : exclusionClassPathPrefix) {
                        if (cName.startsWith(classPathPrefix)) {
                            continue outer;
                        }
                    }
                }
                if (inclusionClassPathPrefix != null && inclusionClassPathPrefix.length > 0) {
                    for (String classPathPrefix : inclusionClassPathPrefix) {
                        if (!cName.startsWith(classPathPrefix)) {
                            continue outer;
                        }
                    }
                }
                stack.push(element.toString());
            }
            while (!stack.isEmpty()) {
                if (sb.length() != 0) {
                    sb.append(">>");
                }
                sb.append(stack.pop());
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    private static String getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf(".");
        if (index == -1) {
            return fullClassName;
        }
        return fullClassName.substring(index + 1, fullClassName.length());
    }

    public static <T> long sumCollectionSize(Collection<T> a, Collection<T> b) {
        return 0L + (a == null ? 0 : a.size()) + (b == null ? 0 : b.size());
    }

    public static <T> long sumSetSize(Set<T> a, Set<T> b) {
        return 0L + (a == null ? 0 : a.size()) + (b == null ? 0 : b.size());
    }

    public static <T> T copyProperties(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T target;

        try {
            target = clazz.newInstance();
            BeanUtils.copyProperties(target, source);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Inner Service error cause by casting to " + clazz);
        }

        return target;
    }

}

package top.yudoge.mybatis.interceptors.pager.utils;

import top.yudoge.mybatis.interceptors.pager.exceptions.InstanceCreateException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

public class PropertiesUtils {
    /**
     * 该方法对于给定类的所有具有公有set方法的属性，去Properties中查找是否有这个属性，如果有，就设置到这个对象中
     * 如果调用公有set方法失败，那么这个属性的设置静默失败
     * @param properties properties对象
     * @param objClz 对象类
     * @return 使用properties填充后的对象
     * @throws InstanceCreateException 当`objClz`类没有一个公有构造方法的话
     * @param <T>
     */
    public static <T> T convertToObject(Properties properties, Class<T> objClz) {
        return readToObject(properties, () -> {
            try {
                return objClz.newInstance();
            } catch (Exception e) {
                throw new InstanceCreateException("Create instance of [" + objClz.getName() + "] faild. Please make sure it has a public constructor with no argument.", e);
            }
        });
    }

    public static Object convertPropertyStringByType(Class type, String propertyString) {
        if (type == Boolean.class || type == boolean.class)
            return Boolean.parseBoolean(propertyString);
        if (type == Integer.class || type == int.class)
            return Integer.parseInt(propertyString);
        if (type == Short.class || type == short.class)
            return Short.parseShort(propertyString);
        if (type == Byte.class || type == byte.class)
            return Byte.parseByte(propertyString);
        if (type == Long.class || type == long.class)
            return Long.parseLong(propertyString);
        if (type == Float.class || type == float.class)
            return Float.parseFloat(propertyString);
        if (type == Double.class || type == double.class)
            return Double.parseDouble(propertyString);
        if (type == String.class)
            return propertyString;
        if (type.isArray() && type.getComponentType() == String.class)
            return propertyString.split(",");
        if (type == List.class)
            return Arrays.asList(propertyString.split(","));
        return null;
    }

    /**
     * 该方法对于给定类的所有具有公有set方法的属性，去Properties中查找是否有这个属性，如果有，就设置到这个对象中
     * 如果调用公有set方法失败，那么这个属性的设置静默失败
     * @param properties properties对象
     * @param factoryMethod 用于获取对象的工厂方法
     * @return 使用properties填充后的对象
     * @param <T>
     */
    public static <T> T readToObject(Properties properties, Supplier<? extends T> factoryMethod) {
        T t = factoryMethod.get();
        PropertyVisitor pv = new PropertyVisitor(t);

        pv.getProperties().stream()
            .filter(pi -> pi.isCanWrite())
            .forEach(pi -> {
                String value = properties.getProperty(pi.getName());
                if (value != null) {
                    try {
                        pi.getWriteMethod().invoke(t, convertPropertyStringByType(pi.getType(), value));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });

        return t;
    }
}

package top.yudoge.mybatis.interceptors.pager.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 一个PropertyItem代表一个JavaBean中的一个属性
 * 该对象中可以获取到属性名，属性的读写方法（也就是get/set）方法，属性上的注解
 * 在常规的用法中，一个类中类中的所有属性，无论它具有什么样的访问权限，有无get/set方法，都会被映射成一个PropertyItem
 */
public class PropertyItem {
    public static final String READ_METHOD_PREFIX = "get";
    public static final String WRITE_METHOD_PREFIX = "set";
    public static final String BOOL_READ_METHOD_PREFIX = "is";

    /**
     * 属性的名字
     */
    private String name;
    private Class type;
    /**
     * 属性上标注的注解，不包括它的读写方法上标注的注解
     */
    private Annotation[] annotations;
    /**
     * 属性是否可读
     * 如果该属性提供了标准get方法，那么该属性可读
     */
    private boolean canRead;
    /**
     * 属性是否可写
     * 如果该属性提供了标准的set方法，那么该属性可写
     */
    private boolean canWrite;
    /**
     * 该属性的读方法
     * 如果该属性提供了get方法，那么是get方法
     */
    private Method readMethod;
    /**
     * 该属性的写方法
     * 如果该属性提供了set方法，那么是set方法
     */
    private Method writeMethod;


    public String getName() {
        return name;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    /**
     * 根据Annotation类型获取属性上的一个Annotation，如果该属性上并没有标注这个Annotation，返回null，否则返回这个Annotation实例
     * @param annotationClz 注解类
     * @return 属性上标注了这个Annotation，返回该Annotation实例，否则返回null
     * @param <T>
     */
    public <T> T getAnnotation(Class<T> annotationClz) {
        for (Annotation annotation : annotations) {
            if (annotation.getClass().getName().equals(annotationClz.getName())) {
                return (T) annotation;
            }
        }
        return null;
    }

    public Class getType() {
        return type;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    protected void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    protected void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    protected void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setType(Class type) {
        this.type = type;
    }

    protected void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    protected void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    @Override
    public String toString() {
        return "PropertyItem{" +
                "name='" + name + '\'' +
                ", annotations=" + Arrays.toString(annotations) +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                ", readMethod=" + readMethod +
                ", writeMethod=" + writeMethod +
                '}';
    }
}

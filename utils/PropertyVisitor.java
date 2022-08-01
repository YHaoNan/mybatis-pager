package top.yudoge.mybatis.interceptors.pager.utils;

import top.yudoge.mybatis.interceptors.pager.utils.naming.BigCamelCaseNamingConverter;
import top.yudoge.mybatis.interceptors.pager.utils.naming.NamingConvert;
import top.yudoge.mybatis.interceptors.pager.utils.naming.SmallCamelCaseNamingConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyVisitor {
    private Map<String, PropertyItem> propertyMap;

    private NamingConvert namingConvert = new NamingConvert(new SmallCamelCaseNamingConverter(), new BigCamelCaseNamingConverter());
    public PropertyVisitor(Object o) {
        propertyMap = new HashMap<>();
        buildPropertyMap(o);
    }

    private boolean isBooleanType(Class clz) {
        return clz == boolean.class || clz == Boolean.class;
    }

    private PropertyItem buildPropertyItem(Object o, Field field) {
        Class objClz = o.getClass();

        PropertyItem propertyItem = new PropertyItem();
        propertyItem.setName(field.getName());
        propertyItem.setType(field.getType());
        propertyItem.setAnnotations(field.getDeclaredAnnotations());


        propertyItem.setCanRead(false);
        propertyItem.setCanWrite(false);

        String methodNameSuffix = null;
        try {
            methodNameSuffix = namingConvert.convert(propertyItem.getName());
        } catch (Exception e) {
            return propertyItem;
        }

        String readMethodName =  PropertyItem.READ_METHOD_PREFIX + methodNameSuffix;
        String writeMethodName = PropertyItem.WRITE_METHOD_PREFIX + methodNameSuffix;

        Method readMethod = null;
        try {
            if (isBooleanType(propertyItem.getType())) {
                String boolReadMethodName =  PropertyItem.BOOL_READ_METHOD_PREFIX + methodNameSuffix;
                readMethod = objClz.getMethod(boolReadMethodName);
            }
        } catch (NoSuchMethodException e) {}

        if (readMethod == null) {
            try {
                readMethod = objClz.getMethod(readMethodName);
            } catch (NoSuchMethodException e) {}
        }
        if (readMethod != null && Modifier.isPublic(readMethod.getModifiers())) {
            propertyItem.setReadMethod(readMethod);
            propertyItem.setCanRead(true);
        }

        try {
            Method writeMethod = objClz.getMethod(writeMethodName, propertyItem.getType());
            if (Modifier.isPublic(writeMethod.getModifiers())) {
                propertyItem.setWriteMethod(writeMethod);
                propertyItem.setCanWrite(true);
            }
        } catch (NoSuchMethodException e) {}

        return propertyItem;

    }


    private void buildPropertyMap(Object o) {
        Class objClz = o.getClass();
        Field[] fields = objClz.getDeclaredFields();
        for (Field field : fields) {
            PropertyItem propertyItem = buildPropertyItem(o, field);
            propertyMap.put(propertyItem.getName(), propertyItem);
        }
    }

    public PropertyItem getProperty(String propertyName) {
        return propertyMap.get(propertyName);
    }

    public List<PropertyItem> getProperties() {
        return propertyMap.values().stream().collect(Collectors.toList());
    }
}

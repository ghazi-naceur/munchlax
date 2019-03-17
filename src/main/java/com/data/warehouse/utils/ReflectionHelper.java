package com.data.warehouse.utils;

import com.data.warehouse.entity.Entity;
import org.springframework.data.annotation.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ghazi Naceur on 17/03/2019
 * Email: ghazi.ennacer@gmail.com
 */

@SuppressWarnings("all")
public final class ReflectionHelper {

    private ReflectionHelper() {
        super();
    }

    public static Map<String, Object> getNonSpecialFields(Entity entity) throws IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();
        Map<String, Object> attributes = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Id.class)) {
                attributes.put(field.getName(), field.get(entity));
            }
        }
        return attributes;
    }

    public static String getEsId(Entity entity) throws IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                return (String) field.get(entity);
            }
        }
        throw new RuntimeException("Entity without ES id !");
    }

    public static String getEsIndex(Entity entity) throws IllegalAccessException, InvocationTargetException {
        for (Annotation annotation : entity.getClass().getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            for (Method method : type.getDeclaredMethods()) {
                Object value = method.invoke(annotation, (Object[]) null);
                if (method.getName().equals("indexName")) {
                    return (String) value;
                }
            }
        }
        throw new RuntimeException("Entity without ES index !");
    }

    public static String getEsType(Entity entity) throws IllegalAccessException, InvocationTargetException {
        for (Annotation annotation : entity.getClass().getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            for (Method method : type.getDeclaredMethods()) {
                Object value = method.invoke(annotation, (Object[]) null);
                if (method.getName().equals("type")) {
                    return (String) value;
                }
            }
        }
        throw new RuntimeException("Entity without ES type !");
    }
}

package com.phazerous.phazerous.db.utils;

import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class DocumentParser {
    public static <T> T parseDocument(Document document, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();

            Class<?> currentClass = clazz;

            while (currentClass != null) {
                Field[] fields = currentClass.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    String fieldName = field.getName();

                    if (List.class.isAssignableFrom(type)) {
                        Type genericType = field.getGenericType();

                        if (genericType instanceof ParameterizedType) {
                            Type rawType = ((ParameterizedType) genericType).getRawType();
                            Class<?> contentType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];

                            if (rawType == List.class) {
                                List<?> documentList = (List<?>) document.get(fieldName);
                                List<Object> newList = (List<Object>) documentList.getClass().newInstance();

                                for (Object object : documentList) {
                                    if (object instanceof Document) {
                                        Object parsedObject = parseDocument((Document) object, contentType);
                                        newList.add(parsedObject);
                                    } else {
                                        newList.add(object);
                                    }
                                }

                                field.set(instance, newList);
                            }
                        }
                    } else {
                        Object fieldValue = document.get(fieldName);

                        if (fieldValue instanceof Document) fieldValue = parseDocument((Document) fieldValue, type);

                        field.set(instance, fieldValue);
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
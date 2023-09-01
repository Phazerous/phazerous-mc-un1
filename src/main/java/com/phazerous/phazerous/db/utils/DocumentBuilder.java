package com.phazerous.phazerous.db.utils;

import org.bson.Document;

import java.lang.reflect.Field;

/**
 * This class is used to prepare a document for insertion into the database.
 * Converts only the fields of not nested and generic types.
 */
public class DocumentBuilder {
    public static <T> Document buildDocument(T entity) {
        Document document = new Document();

        Class<?> currentClass = entity.getClass();

        try {
            while (currentClass != null) {
                Field[] fields = currentClass.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);

                    String fieldName = field.getName();
                    Object fieldValue = field.get(entity);

                    document.append(fieldName, fieldValue);
                }

                currentClass = currentClass.getSuperclass();
            }

            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

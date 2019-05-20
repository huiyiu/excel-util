package com.hyu.excel;

import java.lang.reflect.Field;

public class Mapper
{
    public static <T> void setValue(final T o, final Field f, final Object v) throws  IllegalAccessException, IllegalArgumentException {
        f.setAccessible(true);
        if (v == null && isBaseType(f.getType())) {
            return;
        }
        f.set(o, v);
    }
    
    public static boolean isBaseType(final Class<?> type) {
        return type == Integer.TYPE || type == Float.TYPE || type == Double.TYPE || type == Character.TYPE || type == Boolean.TYPE;
    }
    
    public static <T> Object getValue(final T o, final Field f) throws  IllegalAccessException, IllegalArgumentException {
        f.setAccessible(true);
        return f.get(o);
    }
}
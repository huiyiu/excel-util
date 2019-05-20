package com.hyu.excel.convertor.impl;

import com.hyu.excel.convertor.IConverter;
import com.hyu.excel.convertor.IConvertor;


public class StringConverter implements IConverter
{
    @Override
    public Object convert(final Object v) {
        return this.asString(v);
    }

    @Override
    public String asString(final Object v) {
        if (v == null) {
            return "";
        }
        return String.valueOf(v);
    }
}

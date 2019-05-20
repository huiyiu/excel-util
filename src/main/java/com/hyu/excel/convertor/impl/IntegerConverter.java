package com.hyu.excel.convertor.impl;

import java.math.*;

public class IntegerConverter extends BigDecimalConverter
{
    @Override
    public Object convert(final Object v) {
        final BigDecimal b = (BigDecimal)super.convert(v);
        return b.setScale(0, 1).intValue();
    }
}
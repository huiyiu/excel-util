package com.hyu.excel.convertor.impl;

public class LongConverter extends BigDecimalConverter
{
    @Override
    public Object convert(final Object v) {
        return this.asLong(v);
    }

    public Long asLong(final Object v) {
        return this.asBigDecimal(v).longValue();
    }

    @Override
    public String asString(final Object v) {
        return String.valueOf(v);
    }
}
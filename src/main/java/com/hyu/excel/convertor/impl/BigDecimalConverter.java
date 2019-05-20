package com.hyu.excel.convertor.impl;

import com.hyu.excel.convertor.IConvertor;
import com.hyu.excel.exception.DataConvertorException;

import java.math.BigDecimal;

public class BigDecimalConverter extends StringConverter
{
    @Override
    public Object convert(final Object v) {
        return this.asBigDecimal(v);
    }
    
    @Override
    public String asString(final Object v) {
        return (v == null) ? "0" : String.valueOf(v);
    }
    
    public BigDecimal asBigDecimal(final Object v) {
        BigDecimal result;
        try {
            result = new BigDecimal(this.asString(v).replaceAll(",", ""));
        }
        catch (Exception e) {
            throw new DataConvertorException("\u6570\u636e\u503c:[" + String.valueOf(v) + "]\u8f6c\u6362\u6210\u6570\u503c\u5931\u8d25\uff01", e);
        }
        return result;
    }
}
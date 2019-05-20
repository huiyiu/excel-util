package com.hyu.excel.convertor;

import com.hyu.excel.exception.DataConvertorException;

public interface IConvertor
{
    Object convert(final Object p0) throws DataConvertorException;
    
    String asString(final Object p0);
}
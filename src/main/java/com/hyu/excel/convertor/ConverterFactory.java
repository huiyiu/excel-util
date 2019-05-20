package com.hyu.excel.convertor;

import com.hyu.excel.convertor.impl.*;
import com.hyu.excel.entity.DataTypeEnum;
import org.apache.commons.lang3.EnumUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConverterFactory
{
    private static Map<DataTypeEnum, IConverter> defConverter;
    private Map<DataTypeEnum, IConverter> regConverter;
    
    private ConverterFactory() {
        this.regConverter = new HashMap<>();
    }
    
    public static ConverterFactory getInstance() {
        return new ConverterFactory();
    }
    
    public static IConverter getDefaultConverter() {
        return ConverterFactory.defConverter.get(DataTypeEnum.STRING);
    }
    
    public ConverterFactory regConverter(final DataTypeEnum dataType, final IConverter converter) {
        if (dataType == null || converter == null) {
            return this;
        }
        this.regConverter.put(dataType, converter);
        return this;
    }
    
    public ConverterFactory unregConverter(final DataTypeEnum dataType) {
        if (dataType == null) {
            return this;
        }
        this.regConverter.remove(dataType);
        return this;
    }
    
    public IConverter getConverter(final DataTypeEnum dataType) {
        if (dataType == null) {
            return getDefaultConverter();
        }
        return Optional.ofNullable(Optional.ofNullable(this.regConverter.get(dataType))
                                  .orElse(ConverterFactory.defConverter.get(dataType)))
                .orElse(getDefaultConverter());
    }
    
    public IConverter getConverter(final int dataType) {
        return this.getConverter(EnumUtils.getEnum(DataTypeEnum.class, String.valueOf(dataType)));
    }
    
    static {
        (ConverterFactory.defConverter = new HashMap<>()).put(DataTypeEnum.STRING, new StringConverter());
        ConverterFactory.defConverter.put(DataTypeEnum.LONG, new LongConverter());
        ConverterFactory.defConverter.put(DataTypeEnum.DATE, new DateConverter("yyyy-MM-dd"));
        ConverterFactory.defConverter.put(DataTypeEnum.BIGDECIMAL, new BigDecimalConverter());
        ConverterFactory.defConverter.put(DataTypeEnum.INTEGER, new IntegerConverter());
        ConverterFactory.defConverter.put(DataTypeEnum.BOOLEAN, new BooleanConverter());
        ConverterFactory.defConverter.put(DataTypeEnum.TIMESTAMP, new TimestampConverter());
    }
}
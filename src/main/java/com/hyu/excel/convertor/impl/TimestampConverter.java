package com.hyu.excel.convertor.impl;

import org.joda.time.DateTime;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Date;

import static java.util.Optional.*;

public class TimestampConverter extends DateConverter
{
    @Override
    public Date asDate(final Object v) {
        if (v instanceof Date) {
            return new Timestamp(((Date)v).getTime());
        }
        if (v == null) {
            return null;
        }
        final String vs = String.valueOf(v);
        if (ObjectUtils.isEmpty(vs)) {
            return null;
        }
        return new DateTime(vs).toDate();
    }
    
    public TimestampConverter(final String pattern) {
        super(ofNullable(pattern).orElse("yyyy-MM-dd HH:mm:ss"));
    }
    
    public TimestampConverter() {
        this("");
    }
}
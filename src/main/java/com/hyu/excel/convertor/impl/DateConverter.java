package com.hyu.excel.convertor.impl;

import com.hyu.excel.exception.DataConvertorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends StringConverter
{
    private static Logger logger;
    private String pattern;

    public DateConverter(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object convert(final Object v) {
        return this.asDate(v);
    }

    public Date asDate(final Object v) {
        if (v != null && v instanceof Date) {
            return (Date)v;
        }
        final String vs = super.asString(v);
        final SimpleDateFormat df_1 = new SimpleDateFormat("yyyy/MM/dd");
        final SimpleDateFormat df_2 = new SimpleDateFormat("yyyyMMdd");
        final SimpleDateFormat df_3 = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat df_4 = new SimpleDateFormat("yyyy-M-d");
        final SimpleDateFormat df_5 = new SimpleDateFormat("yyyy.MM.dd");
        final SimpleDateFormat df_6 = new SimpleDateFormat("yyMMdd");
        Date conversDate = null;
        try {
            conversDate = df_1.parse(vs);
        }
        catch (ParseException e) {
            try {
                conversDate = df_2.parse(vs);
            }
            catch (ParseException e2) {
                try {
                    conversDate = df_3.parse(vs);
                }
                catch (ParseException e3) {
                    try {
                        conversDate = df_4.parse(vs);
                    }
                    catch (ParseException e4) {
                        try {
                            conversDate = df_5.parse(vs);
                        }
                        catch (ParseException e5) {
                            try {
                                conversDate = df_6.parse(vs);
                            }
                            catch (ParseException e6) {
                                throw new DataConvertorException("\u6570\u636e\u503c:[" + String.valueOf(v) + "]\u8f6c\u6362\u6210Date\u5931\u8d25\uff01", e);
                            }
                        }
                    }
                }
            }
        }
        return conversDate;
    }

    @Override
    public String asString(final Object v) {
        if (v == null) {
            return "";
        }
        if (v instanceof String) {
            return (String)v;
        }
        if (v instanceof Date) {
            try {
                return new SimpleDateFormat(this.pattern).format(this.convert(v));
            }
            catch (Exception e) {
                DateConverter.logger.info(String.format("\u65e5\u671f\u6570\u636e\u3010%s\u3011\u683c\u5f0f\u5316\u3010%s\u3011\u5931\u8d25\uff1a%s", v, this.pattern, e.getMessage()));
            }
        }
        return super.asString(v);
    }

    static {
        DateConverter.logger = LoggerFactory.getLogger((Class)DateConverter.class);
    }
}

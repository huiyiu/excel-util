package com.hyu.excel.convertor.impl;

import java.util.Optional;

public class BooleanConverter extends StringConverter
{
    private String trueText;
    private String falseText;
    
    public BooleanConverter() {
        this.trueText = "\u662f";
        this.falseText = "\u5426";
    }
    
    public BooleanConverter(final String trueText, final String falseText) {
        this.trueText = "\u662f";
        this.falseText = "\u5426";
        this.trueText = Optional.ofNullable(trueText).orElse(this.trueText);
        this.falseText = Optional.ofNullable(trueText).orElse(this.falseText);
    }
    @Override
    public Object convert(final Object v) {
        final String vs = super.asString(v);
        return ("true".equalsIgnoreCase(vs) || "1".equals(vs)) || vs.equals(this.trueText);
    }
    
    @Override
    public String asString(final Object v) {
        final boolean vb = (boolean)this.convert(v);
        return vb ? this.trueText : this.falseText;
    }
}
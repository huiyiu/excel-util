package com.hyu.excel.convertor.impl;

import com.hyu.excel.convertor.IRenderer;

import java.util.Optional;

public class TextMapperRenderer implements IRenderer
{
    private String[] values;
    
    public TextMapperRenderer() {
    }
    
    public TextMapperRenderer(final String[] values) {
        this.values = values;
    }
    
    @Override
    public String render(final String v) {
        try {
            return this.values[Optional.ofNullable(v).map(vStr ->Integer.parseInt(vStr)).orElse(0)];
        }
        catch (Exception e) {
            return v;
        }
    }
}
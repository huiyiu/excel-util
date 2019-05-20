package com.hyu.excel.convertor.impl;

import com.hyu.excel.convertor.IRenderer;

public class ConstRenderer implements IRenderer
{
    private String text;
    
    public ConstRenderer(final String text) {
        this.text = text;
    }
    
    @Override
    public String render(final String v) {
        return this.text;
    }
}
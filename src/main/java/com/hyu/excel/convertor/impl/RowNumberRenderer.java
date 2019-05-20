package com.hyu.excel.convertor.impl;

import com.hyu.excel.convertor.IRenderer;

public class RowNumberRenderer implements IRenderer
{
    private int rowIndex;
    
    public RowNumberRenderer() {
        this.rowIndex = 1;
    }
    
    public void reset() {
        this.rowIndex = 1;
    }
    
    @Override
    public String render(final String v) {
        return String.valueOf(this.rowIndex++);
    }
}
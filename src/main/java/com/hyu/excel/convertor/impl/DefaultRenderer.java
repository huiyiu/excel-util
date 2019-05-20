package com.hyu.excel.convertor.impl;

import com.hyu.excel.convertor.IRenderer;

import java.util.Optional;

public class DefaultRenderer implements IRenderer
{
    @Override
    public String render(final String v) {
        return Optional.ofNullable(v).orElse("");
    }
}
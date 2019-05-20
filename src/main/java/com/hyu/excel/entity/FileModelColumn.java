package com.hyu.excel.entity;

import com.hyu.excel.convertor.IRenderer;
import com.hyu.excel.convertor.impl.DefaultRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class FileModelColumn
{
    private Long id;
    private Long modelId;
    private Integer col;
    private Integer dataType;
    private String columnTitle;
    private String description;
    private Integer seq;
    private String verifyExp;
    private IRenderer renderer;

    public FileModelColumn() {
        this.renderer = new DefaultRenderer();
    }

    public FileModelColumn(final Integer col, final Integer dataType, final String columnTitle, final String description, final String verifyExp) {
        this(col, dataType, columnTitle, description, verifyExp, null);
    }

    public FileModelColumn(final Integer col, final Integer dataType, final String columnTitle, final String description, final String verifyExp, final IRenderer renderer) {
        this.col = col;
        this.dataType = dataType;
        this.columnTitle = columnTitle;
        this.description = description;
        this.verifyExp = verifyExp;
        this.renderer = Optional.ofNullable(renderer).orElse(new DefaultRenderer());
    }
}
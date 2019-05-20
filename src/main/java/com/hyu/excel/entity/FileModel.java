package com.hyu.excel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class FileModel
{
    private Long id;
    private String name;
    private Integer startRow;
    private String description;
    private Integer seq;
    private List<FileModelColumn> fileModelColumns;
}

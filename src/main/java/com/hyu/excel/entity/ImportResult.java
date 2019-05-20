package com.hyu.excel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class ImportResult<T>
{
    private List<T> successResut;
    private List<Map<String, Object>> errorResult;
    
    public ImportResult() {
        this.successResut = new ArrayList<>();
        this.errorResult = new ArrayList<>();
    }

    
    public ImportResult<T> addSuccessResult(final T o) {
        this.successResut.add(o);
        return this;
    }
    
    public ImportResult<T> addErrorResult(final Map<String, Object> m) {
        this.errorResult.add(m);
        return this;
    }
}
package com.hyu.excel.ds;

import com.hyu.excel.entity.FileModel;
import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.exception.DataException;
import com.hyu.excel.exception.EndOfFlowException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayMapDataSource extends DataSource<HashMap>
{
    private List<HashMap> list;
    
    public ArrayMapDataSource(final List<HashMap> list, final Class<HashMap> c) {
        super(c);
        this.list = null;
        this.list = list;
    }
    
    @Override
    public void onInit() {
    }
    
    @Override
    public Object getColumnValue(final int row, final FileModelColumn mc) {
        final Map<?, ?> map = (Map<?, ?>)this.getRow(null, row);
        return map.get(mc.getColumnTitle());
    }
    
    @Override
    public HashMap getRow(final FileModel model, final int row) throws EndOfFlowException, DataException {
        if (row >= this.list.size()) {
            throw new EndOfFlowException();
        }
        return this.list.get(row);
    }
    
    @Override
    public void release() {
        this.list.clear();
        this.list = null;
    }
}
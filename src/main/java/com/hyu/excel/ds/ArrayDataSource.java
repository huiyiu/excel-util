package com.hyu.excel.ds;

import com.hyu.excel.Mapper;
import com.hyu.excel.entity.FileModel;
import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.exception.EndOfFlowException;

import java.util.List;

public class ArrayDataSource<T> extends DataSource<T>
{
    private List<T> list;
    
    public ArrayDataSource(final List<T> list, final Class<T> c) {
        super(c);
        this.list = null;
        this.list = list;
    }
    
    @Override
    public void onInit() {
    }
    
    @Override
    public Object getColumnValue(final int row, final FileModelColumn mc) {
        final T rowDate = this.getRow(null, row);
        Object result = null;
        try {
            result = Mapper.getValue(rowDate, rowDate.getClass().getDeclaredField(mc.getColumnTitle()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public T getRow(final FileModel model, final int row) throws EndOfFlowException {
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

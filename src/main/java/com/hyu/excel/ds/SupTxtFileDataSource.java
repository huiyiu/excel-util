package com.hyu.excel.ds;

import com.hyu.excel.entity.FileModelColumn;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.*;

public class SupTxtFileDataSource<T> extends TxtFileDataSource<T> {
    public SupTxtFileDataSource(final Class<T> c, final MultipartFile mf) {
        super(c, mf);
    }

    public SupTxtFileDataSource(final Class<T> c, final String fn) {
        super(c, fn);
    }

    public SupTxtFileDataSource(final Class<T> c, final String fn, final String separator) {
        super(c, fn, separator);
    }

    @Override
    public Object getColumnValue(final int row, final FileModelColumn mc) {
        final String cv = String.valueOf(super.getColumnValue(row, mc));
        return ObjectUtils.isEmpty(cv) ? cv : cv.replaceAll("=\"(.*?)\"", "$1");
    }
}
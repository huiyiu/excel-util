package com.hyu.excel.ds;

import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.exception.EndOfFlowException;
import com.hyu.excel.exception.ParseFileException;
import org.springframework.web.multipart.*;
import java.io.*;
import jxl.*;
import org.slf4j.*;

public class ExcelFileDataSource<T> extends DataSource<T>
{
    private static Logger logger;
    private Sheet st;
    private Workbook rwb;
    private MultipartFile mf;
    private String fn;
    
    public ExcelFileDataSource(final Class<T> c, final MultipartFile mf) {
        super(c);
        this.st = null;
        this.rwb = null;
        this.mf = null;
        this.fn = null;
        this.mf = mf;
    }
    
    public ExcelFileDataSource(final Class<T> c, final String fn) {
        super(c);
        this.st = null;
        this.rwb = null;
        this.mf = null;
        this.fn = null;
        this.fn = fn;
    }
    
    @Override
    public void onInit() {
        try {
            ExcelFileDataSource.logger.debug("\u6570\u636e\u6e90\u521d\u59cb\u5316...");
            if (this.fn == null) {
                this.rwb = Workbook.getWorkbook(this.mf.getInputStream());
                this.st = this.rwb.getSheet(0);
            }
            else {
                this.rwb = Workbook.getWorkbook(new File(this.fn));
                this.st = this.rwb.getSheet(0);
            }
        }
        catch (Exception e) {
            throw new ParseFileException();
        }
    }
    
    @Override
    public Object getColumnValue(final int row, final FileModelColumn mc) {
        Cell[] cell = null;
        try {
            cell = this.st.getRow(row - 1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new EndOfFlowException();
        }
        String columnValue = "";
        if (cell.length > mc.getCol() - 1) {
            columnValue = cell[mc.getCol() - 1].getContents();
        }
        return columnValue;
    }
    
    @Override
    public void release() {
        this.rwb.close();
        this.mf = null;
        this.st = null;
    }
    
    static {
        ExcelFileDataSource.logger = LoggerFactory.getLogger(ExcelFileDataSource.class);
    }
}

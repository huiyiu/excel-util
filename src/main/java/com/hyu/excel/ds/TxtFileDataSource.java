package com.hyu.excel.ds;

import com.hyu.excel.entity.FileModel;
import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.exception.EndOfFlowException;
import com.hyu.excel.exception.ParseFileException;
import org.springframework.web.multipart.*;
import java.io.*;
import org.slf4j.*;

public class TxtFileDataSource<T> extends DataSource<T>
{
    private static Logger logger;
    private BufferedReader br;
    private MultipartFile mf;
    private String[] sources;
    private String fn;
    private String separator;
    
    public TxtFileDataSource(final Class<T> c, final MultipartFile mf) {
        super(c);
        this.br = null;
        this.mf = null;
        this.sources = null;
        this.fn = null;
        this.separator = ",";
        this.mf = mf;
    }
    
    public TxtFileDataSource(final Class<T> c, final String fn) {
        this(c, fn, ",");
    }
    
    public TxtFileDataSource(final Class<T> c, final String fn, final String separator) {
        super(c);
        this.br = null;
        this.mf = null;
        this.sources = null;
        this.fn = null;
        this.separator = ",";
        this.fn = fn;
        this.separator = separator;
    }
    
    @Override
    public void onInit() {
        try {
            TxtFileDataSource.logger.debug("\u6570\u636e\u6e90\u521d\u59cb\u5316...");
            if (this.fn == null) {
                this.br = new BufferedReader(new InputStreamReader(this.mf.getInputStream(), "gbk"));
            }
            else {
                this.br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fn), "gbk"));
            }
        }
        catch (IOException e) {
            TxtFileDataSource.logger.error("\u6570\u636e\u6e90\u89e3\u6790\u51fa\u73b0\u5f02\u5e38\uff0cerrorMsg\uff1a{}", (Object)e.getMessage());
            throw new ParseFileException(e.getMessage());
        }
    }
    
    @Override
    public Object getColumnValue(final int row, final FileModelColumn mc) {
        if (mc.getCol() > this.sources.length || mc.getCol() <= 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("\u5f53\u524d\u8bfb\u53d6\u7b2c\u3010%s\u3011\u884c\uff0c\u7b2c\u3010%s\u3011\u5217", row, mc.getCol()));
        }
        return this.sources[mc.getCol() - 1];
    }
    
    @Override
    public T getRow(final FileModel model, final int row) throws EndOfFlowException {
        TxtFileDataSource.logger.debug("\u6570\u636e\u6e90\u8fdb\u884c\u884c\u8bfb\u53d6...");
        try {
            this.sources = this.br.readLine().split(this.separator);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new EndOfFlowException();
        }
        return super.getRow(model, row);
    }
    
    @Override
    public void skip() {
        try {
            this.br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void release() {
        TxtFileDataSource.logger.debug("\u6570\u636e\u6e90\u91ca\u653e...");
        try {
            if (this.br != null) {
                this.br.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.mf = null;
        this.br = null;
    }
    
    static {
        TxtFileDataSource.logger = LoggerFactory.getLogger((Class)TxtFileDataSource.class);
    }
}

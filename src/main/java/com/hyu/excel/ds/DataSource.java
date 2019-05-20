package com.hyu.excel.ds;

import com.google.gson.reflect.*;
import com.hyu.excel.ImportExportManager;
import com.hyu.excel.Mapper;
import com.hyu.excel.convertor.ConverterFactory;
import com.hyu.excel.entity.FileModel;
import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.exception.DataConvertorException;
import com.hyu.excel.exception.DataException;
import com.hyu.excel.exception.EndOfFlowException;
import com.hyu.excel.exception.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;

public abstract class DataSource<T> extends TypeToken<T>
{
    private ConverterFactory converterFactory;
    private static Logger logger;
    private boolean inited;
    private Class<T> c;

    public void bindModel(final Class<T> c) {
        this.c = c;
    }

    public DataSource(final Class<T> c) {
        this.inited = false;
        this.bindModel(c);
    }

    public boolean isInited() {
        return this.inited;
    }

    public void init() {
        this.inited = true;
        this.onInit();
    }

    public abstract void onInit();

    public void skip() {
    }

    public abstract Object getColumnValue(final int p0, final FileModelColumn p1);


    public T getRow(final FileModel model, final int row) throws EndOfFlowException, DataException {
        T o = null;
        try {
            o = this.c.newInstance();
        }
        catch (Exception e) {
            DataSource.logger.error(e.getMessage(), (Throwable)e);
            throw new EndOfFlowException();
        }
        DataException de = null;
        final Map<String, Object> vs = new HashMap<String, Object>();
        for (final FileModelColumn mc : model.getFileModelColumns()) {
            final Object v = this.getColumnValue(row, mc);
            vs.put(mc.getColumnTitle(), v);
            try {
                Mapper.setValue(o, this.c.getDeclaredField(mc.getColumnTitle()), this.getConverterFactory().getConverter(mc.getDataType()).convert(this.verify(String.valueOf(v), mc.getVerifyExp())));
            }
            catch (VerificationException e2) {
                if (de != null) {
                    continue;
                }
                de = new VerificationException("\u7b2c" + row + "\u884c," + mc.getColumnTitle() + e2.getMessage());
                vs.put("errorMsg", mc.getColumnTitle() + e2.getMessage());
                de.setSource(vs);
            }
            catch (DataConvertorException e3) {
                if (de != null) {
                    continue;
                }
                de = new DataConvertorException("\u7b2c" + row + "\u884c\u6570\u636e\u8f6c\u6362\u51fa\u9519\uff0cvalue:" + String.valueOf(v));
                vs.put("errorMsg", mc.getColumnTitle() + e3.getMessage());
                de.setSource(vs);
            }
            catch (Exception e4) {
                DataSource.logger.error(e4.getMessage(), (Throwable)e4);
            }
        }
        if (de != null) {
            throw de;
        }
        return o;
    }

    public abstract void release();


    public ConverterFactory getConverterFactory() {
        return this.converterFactory;
    }

    private String verify(String verifyStr, final String rgex) throws VerificationException {

        String vStr = Optional.ofNullable(verifyStr).orElse("");
        if (ObjectUtils.isEmpty(vStr)) {
            return vStr;
        }
        try {
            if (vStr.matches(rgex)) {
                return verifyStr;
            }
            throw new VerificationException(String.format("\u6570\u636e\u6821\u9a8c\u51fa\u9519\uff1a[%s] \u5339\u914d [%s]\u5931\u8d25\u3002", verifyStr, rgex));
        }
        catch (PatternSyntaxException e) {
            DataSource.logger.warn(e.getMessage(), e);
            throw new VerificationException(String.format("\u6821\u9a8c\u683c\u5f0f\u9519\u8bef\uff1a[%s]\u3002", rgex));
        }
    }

    static {
        DataSource.logger = LoggerFactory.getLogger(ImportExportManager.class);
    }
}

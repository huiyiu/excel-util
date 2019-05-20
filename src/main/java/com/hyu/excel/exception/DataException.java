package com.hyu.excel.exception;

import java.util.*;

public class DataException extends RuntimeException
{
    private static final long serialVersionUID = 2785736378991392221L;
    private String message;
    private Map<String, Object> source;
    
    public DataException(final String msg) {
        super(msg);
        this.source = new HashMap<String, Object>();
        this.message = msg;
    }
    
    public DataException(final String msg, final Throwable cause) {
        super(msg, cause);
        this.source = new HashMap<String, Object>();
        this.message = msg;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String msg) {
        this.message = msg;
    }
    
    public void appendMessage(final String msg) {
        this.message = this.message + "," + msg;
    }
    
    public Map<String, Object> getSource() {
        return this.source;
    }
    
    public void setSource(final Map<String, Object> source) {
        this.source = source;
    }
}

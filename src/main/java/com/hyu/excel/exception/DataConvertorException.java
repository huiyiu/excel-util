package com.hyu.excel.exception;

public class DataConvertorException extends DataException
{
    private static final long serialVersionUID = -8195101560447187968L;
    
    public DataConvertorException(final String msg) {
        super(msg);
    }
    
    public DataConvertorException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
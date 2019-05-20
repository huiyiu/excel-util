package com.hyu.excel.exception;

public class VerificationException extends DataException
{
    private static final long serialVersionUID = -8195101560447187968L;
    
    public VerificationException(final String msg) {
        super(msg);
    }
    
    public VerificationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
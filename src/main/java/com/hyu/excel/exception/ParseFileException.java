package com.hyu.excel.exception;

public class ParseFileException extends RuntimeException
{
    public ParseFileException() {
        super("\u6587\u4ef6\u89e3\u6790\u5f02\u5e38");
    }
    
    public ParseFileException(final String msg) {
        super(msg);
    }
}
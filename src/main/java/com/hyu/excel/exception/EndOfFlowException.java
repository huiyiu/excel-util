package com.hyu.excel.exception;

public class EndOfFlowException extends RuntimeException
{
    private static final long serialVersionUID = 3306914728735324588L;
    
    public EndOfFlowException() {
        super("\u6570\u636e\u6e90\u5df2\u7ecf\u7ed3\u675f");
    }
}
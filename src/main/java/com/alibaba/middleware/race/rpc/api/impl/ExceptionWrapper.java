package com.alibaba.middleware.race.rpc.api.impl;

import java.io.Serializable;


public class ExceptionWrapper implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Throwable throwable;
    String exceptionClass;
    
    public Throwable getThrowable() {
        return throwable;
    }
    
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
    
    public String getExceptionClass() {
        return exceptionClass;
    }
    
    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public ExceptionWrapper(Throwable throwable, String exceptionClass){
        super();
        this.throwable = throwable;
        this.exceptionClass = exceptionClass;
    }
    
    
}

package com.alibaba.middleware.race.rpc.async;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



public class MyFuture implements Future{
    
    Object result;
    
    public Object getResult() {
        return result;
    }

    
    public void setResult(Object result) {
        this.result = result;
    }

    public MyFuture(){
        
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
      
        return  false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        while(result==null){
            Thread.sleep(50);
        }
        return result;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException{
        long millsecond=unit.convert(timeout, TimeUnit.MILLISECONDS);
        while(result==null && millsecond>0){
            Thread.sleep(50);
            millsecond-=50;
        }
        return result;
    }


}

package com.alibaba.middleware.race.rpc.api.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;

/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcConsumerImpl extends RpcConsumer {
    private Class<?> interfaceClass;
    private String version;
    private int clientTimeout;
    private ConsumerHook hook;

    @Override
    public RpcConsumer interfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        //find remote how to?
        return this;
    }

    @Override
    public RpcConsumer version(String version) {
        this.version = version;
        return this;
    }

    @Override
    public RpcConsumer clientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
        return this;
    }

    @Override
    public RpcConsumer hook(ConsumerHook hook) {
        // TODO Auto-generated method stub
        this.hook = hook;
        return this;
    }

    @Override
    public Object instance() {
        // TODO Auto-generated method stub
        return super.instance();
    }

    @Override
    public void asynCall(String methodName) {
        // TODO Auto-generated method stub
        super.asynCall(methodName);
    }

    @Override
    public <T extends ResponseCallbackListener> void asynCall(String methodName, T callbackListener) {
        // TODO Auto-generated method stub
        super.asynCall(methodName, callbackListener);
    }

    @Override
    public void cancelAsyn(String methodName) {
        // TODO Auto-generated method stub
        super.cancelAsyn(methodName);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*
         * 首先当然是判断NPE啦
         * 使用代理连接
         * 准备object output stream之类的  或者用框架
         * 传输啦
         * 这里有的数据：
         * Method
         * 
         */


        return super.invoke(proxy, method, args);
    }

    class ConsumerHandler implements InvocationHandler{
        private Object target=null;

        ConsumerHandler(Object target){
            this.target=target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before process...");
            Object result=method.invoke(target,args);
            System.out.println("after process...");
            return null;
        }
    }

}





























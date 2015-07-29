package com.alibaba.middleware.race.rpc.api.impl;

import com.alibaba.middleware.race.rpc.api.RpcProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcProviderimpl extends RpcProvider {
    private static final String HOST= "127.0.0.1";
    private static final int PORT = 9999;

    private Class<?> serviceInterface;
    private String version;
    private int timeout;
    private String serializeType;
    Object serviceInstance;
    @Override
    public RpcProvider serviceInterface(Class<?> serviceInterface) {
        this.serviceInterface=serviceInterface;
        return this;
    }

    @Override
    public RpcProvider version(String version) {
        this.version = version;
        return this;
    }

    @Override
    public RpcProvider impl(Object serviceInstance) {
        this.serviceInstance=serviceInstance;
        return this;
    }

    @Override
    public RpcProvider timeout(int timeout) {
        this.timeout=timeout;
        return super.timeout(timeout);
    }

    @Override
    public RpcProvider serializeType(String serializeType) {
        this.serializeType=serializeType;
        return this;
    }

    @Override
    public void publish() throws InvocationTargetException, IllegalAccessException {
        //调用服务模块启动 
        //我感觉这里要做的事情是
        /*
         * 开线程 开服务
         * 不停监听端口
         * 有传送 invoke具体方法
         * 
         *  这一块感觉不一定是在这的
         *  
         * 接收到发过来的method
         * 调用method的具体invoke
         * 
         * 回调
         */

        //建立网络连接 监听中 - - -
        String methodName="";
        Class<?>[] parameterTypes={String.class};
        Object[] arguments={new String("abc")};

        try {
            Method method=serviceInstance.getClass().getMethod(methodName,parameterTypes);   //获得对应的方法然后
            method.invoke(serviceInstance,arguments);                                     //然后利用得到的参数调用得到的方法
        } catch (NoSuchMethodException e) {

        }
        //计算得到结果将结果放到输出流中

        //监听中 - - -
        super.publish();
    }
    
}


















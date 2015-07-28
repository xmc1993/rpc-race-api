package com.alibaba.middleware.race.rpc.api.impl;

import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.api.RpcProvider;

/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcProviderimpl extends RpcProvider {
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
        return super.timeout(timeout);
    }

    @Override
    public RpcProvider serializeType(String serializeType) {
        this.serializeType=serializeType;
        return this;
    }

    @Override
    public void publish() {
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
        super.publish();
    }
    
}

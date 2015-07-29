package com.alibaba.middleware.race.rpc.api.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        /**
         * set客户端要调用的接口类型
         */
        this.interfaceClass = interfaceClass;
        return this;
    }

    @Override
    public RpcConsumer version(String version) {
        /**
         * 设置服务的版本号？（有什么意义？
         */
        this.version = version;
        return this;
    }

    @Override
    public RpcConsumer clientTimeout(int clientTimeout) {
        /**
         * 设置clientTimeout的时间(server那边也设置了timeout有什么联系？
         */
        this.clientTimeout = clientTimeout;
        return this;
    }

    @Override
    public RpcConsumer hook(ConsumerHook hook) {
        /**
         * 设置用户的 hook 实现
         */
        this.hook = hook;
        return this;
    }

    @Override
    public Object instance() {
        /**
         * 返回给客户端所请求的服务类型实例的代理
         * param1:不太理解为什么是this.getClass().getClassLoader()? 似乎有点明白了...
         * param2:所有接口类的list
         * param3: InvocationHandler实例
         */
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{this.interfaceClass}, this);
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
        /**
         * ConsumerImpl会返回给客户端一个代理类
         * 当客户端通过代理调用函数的时候
         * 代理类对象就会通过invoke方法来调用相应的方法
         * Client端并没有服务类型的实例
         */
          System.out.println("调用方法前- - - -");

          Object result=null;



          System.out.println("调用方法前- - - -");

          return result;
    }


}





























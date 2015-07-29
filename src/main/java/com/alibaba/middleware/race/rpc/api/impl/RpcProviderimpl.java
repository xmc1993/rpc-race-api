package com.alibaba.middleware.race.rpc.api.impl;

import com.alibaba.middleware.race.rpc.api.RpcProvider;
import com.alibaba.middleware.race.rpc.api.netty.NettyServer;
import io.netty.channel.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcProviderimpl extends RpcProvider {
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
        final String[] methodName = new String[0];
        final Class<?>[][] parameterTypes = new Class<?>[1][1];
        final Object[][] arguments = new Object[0][];

        NettyServer nettyServer=new NettyServer(PORT,new ChannelInitializer<Channel>(){                    //传入port和channel两个参数建立一个Server实例

            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                        if(o instanceof String){       //如果传过来的是方法名
                            methodName[0] =(String) o;
                        }else if(o instanceof  Class<?>[]){    //如果传过来的是参数的类型数组
                            parameterTypes[0] =( Class<?>[])o;
                        }else if(o instanceof Object[]){        //如果传过来的是参数数组
                            arguments[0] =(Object[])o;
                            try {
                                Method method=serviceInstance.getClass().getMethod(methodName[0], parameterTypes[0]);   //获得对应的方法然后
                                Object result=method.invoke(serviceInstance, arguments[0]);                                          //然后利用得到的参数调用得到的方法

                                channelHandlerContext.writeAndFlush(result).sync();              //将调用结果使用Netty进行传输

                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

                    }
                });
            }
        });


        try {
            nettyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }



        //计算得到结果将结果放到输出流中

        //监听中 - - -
        super.publish();
    }
    
}


















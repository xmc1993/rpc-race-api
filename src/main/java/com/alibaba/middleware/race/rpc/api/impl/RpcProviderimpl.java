package com.alibaba.middleware.race.rpc.api.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.middleware.race.rpc.api.RpcProvider;
import com.alibaba.middleware.race.rpc.api.netty.NettyServer;
import com.alibaba.middleware.race.rpc.context.RpcContext;
import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;

/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcProviderImpl extends RpcProvider {
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

        NettyServer nettyServer=new NettyServer(PORT,new ChannelInitializer<Channel>(){                    //传入port和channel两个参数建立一个Server实例

            @Override
            protected void initChannel(Channel channel) throws Exception {
            	 channel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),new ObjectEncoder());
                channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                       
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                        if(o instanceof RpcRequest){
                            RpcRequest request=(RpcRequest)o;
                            String methodName=request.getMethodName();
                            Class<?>[] parameterTypes=request.getParamTypes();
                            Object[] arguments=request.getParams();
                            RpcResponse response=new RpcResponse();
                            RpcContext.setLocalProps(request.getProp());
                            try {
                                Method method=serviceInstance.getClass().getMethod(methodName, parameterTypes);   //获得对应的方法然后
                                Object result=method.invoke(serviceInstance, arguments);                                          //然后利用得到的参数调用得到的方法
                                response.setAppResponse(result);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }  catch (Exception e) {
                                // 异常也需要传出去
                                InvocationTargetException targetEx = (InvocationTargetException)e; 
                                Throwable t = targetEx .getTargetException();
                                response.setAppResponse(t);
                                response.setErrorMsg(t.getClass().getName());
                            }
                            response.setProp(RpcContext.getProps());
                            channelHandlerContext.writeAndFlush(response).sync();
                        }
                        //如果传过来的不是rpcrequest，那我也不知道为啥了，就不管他了
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
                    	channelHandlerContext.close();  
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                    	//打印异常信息并关闭连接  
                    	throwable.printStackTrace();  
                    	channelHandlerContext.close();  
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


















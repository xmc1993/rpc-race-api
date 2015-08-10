package com.alibaba.middleware.race.rpc.api.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.async.MyFuture;
import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;
import com.alibaba.middleware.race.rpc.async.ResponseFuture;
import com.alibaba.middleware.race.rpc.context.RpcContext;
import com.alibaba.middleware.race.rpc.encode.KryoDecoder;
import com.alibaba.middleware.race.rpc.encode.KryoEncoder;
import com.alibaba.middleware.race.rpc.encode.KryoPool;
import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;
/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcConsumerImpl extends RpcConsumer {
    private  static final String HOST = System.getProperty("SIP");
    private static final int PORT = 9999;
    private Class<?> interfaceClass;
    private String version;
    private int clientTimeout;
    private ConsumerHook hook;
    String asyncMethod;
    String asyncCallbackMethod;
    Bootstrap b;
    BlockingQueue<ResponseCallbackListener> callbackQueue=new LinkedBlockingQueue<ResponseCallbackListener>();
    Object tmp;
    KryoPool pool=new KryoPool();

    public RpcConsumerImpl(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    ;

            //如果之前设置了异步调用
//          if (asyncMethod == method.getName()) {
//              ChannelFuture f = b.connect(HOST, PORT);
//              myFuture=new MyFuture(f);
//              ResponseFuture.setFuture(myFuture);
//
//                  asyncMethod=null;
//                  return null;
//
//          }

        } finally {
//            workerGroup.shutdownGracefully();
        }
    }

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
        asyncMethod=methodName;
        super.asynCall(methodName);
    }

    @Override
    public <T extends ResponseCallbackListener> void asynCall(String methodName, T callbackListener) {
        if(callbackListener==null)return;
        asyncCallbackMethod=methodName;
        callbackQueue.add(callbackListener);
        super.asynCall(methodName, callbackListener);
    }

    @Override
    public void cancelAsyn(String methodName) {
        asyncMethod=null;
        asyncCallbackMethod=null;
        super.cancelAsyn(methodName);
    }

//    Method lastmethod;
//    Object[] lastargs;
//    Object lastResult;
//    HashMap<String,Object> lastProp;

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        final MyFuture myFuture=asyncMethod==method.getName()?new MyFuture():null;
        final ResponseCallbackListener listener=asyncCallbackMethod==method.getName()?callbackQueue.take():null;
        final RpcRequest request=new RpcRequest(RpcContext.getLocalProps(), method.getName(), method.getParameterTypes(), args);
        if(hook!=null){
            hook.before(request);
        }
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new KryoDecoder(pool),new KryoEncoder(pool),new ObjectClientHandler(request,myFuture,listener));
//                ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),new ObjectEncoder(),new ObjectClientHandler(request,myFuture,listener));
            }
        });
        if(asyncMethod==method.getName()){
            ResponseFuture.setFuture(myFuture);
            asyncMethod=null;
            b.connect(HOST, PORT).sync();
            return null;
        }
        if(asyncCallbackMethod==method.getName()){
            asyncCallbackMethod=null;
            b.connect(HOST, PORT).sync();
            return null;
        }
//        //我太无耻了
//        if(method.equals(lastmethod)&&Arrays.equals(lastargs, args)){
//            if(RpcContext.getLocalProps().keySet().equals(lastProp.keySet())){
//                return lastResult;
//            }
//        }

        ChannelFuture f = b.connect(HOST, PORT).sync();
        f.channel().closeFuture().sync();
          //I don't have a better way to do this
          //If u have any idea, please contact me
          RpcResponse result=(RpcResponse)tmp;
          if(result.isError()){
              throw (Exception)Class.forName(result.getErrorMsg()).cast(result.getAppResponse());
          }
//          lastmethod=method;
//          lastargs=args;
//          lastProp=(HashMap<String, Object>) RpcContext.getLocalProps().clone();
//          lastResult=result.getAppResponse();
          return result.getAppResponse();
    }

    public class ObjectClientHandler extends ReadTimeoutHandler {
    	  MyFuture myFuture;
    	  ResponseCallbackListener listener;
    	  RpcRequest request;

        public ObjectClientHandler(RpcRequest request, MyFuture myFuture, ResponseCallbackListener listener){
            super(clientTimeout,TimeUnit.MILLISECONDS);
            this.myFuture=myFuture;
            this.listener=listener;
            this.request=request;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(request);
            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            synchronized (RpcConsumerImpl.this){
//          	  RpcConsumerImpl.this.notifyAll();
//            }
            tmp=msg!=null?msg:tmp;
            ctx.close();
            if(myFuture!=null){
                myFuture.setResult(msg);
            }
            if(listener!=null){
                RpcResponse result=(RpcResponse)msg;
                if(result.isError()){
                    listener.onException((Exception)Class.forName(result.getErrorMsg()).cast(result.getAppResponse()));
                    return;
                }
                listener.onResponse(result.getAppResponse());
            }
            if(hook!=null){
                hook.after(request);
            }

            super.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
            super.exceptionCaught(ctx, cause);
        }


    }
}





























package com.alibaba.middleware.race.rpc.api.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;

/**
 * Created by Administrator on 2015/7/28.
 */
public class RpcConsumerImpl extends RpcConsumer {
    private  static final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private Class<?> interfaceClass;
    private String version;
    private int clientTimeout;
    private ConsumerHook hook;
    private Object returnObj;
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
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        /**
         * ConsumerImpl会返回给客户端一个代理类
         * 当客户端通过代理调用函数的时候
         * 代理类对象就会通过invoke方法来调用相应的方法
         * Client端并没有服务类型的实例
         */
          EventLoopGroup workerGroup = new NioEventLoopGroup();  
          try {  
              Bootstrap b = new Bootstrap();  
              b.group(workerGroup)  
                      .channel(NioSocketChannel.class)  
                      .option(ChannelOption.SO_KEEPALIVE, true)  
                      .handler(new ChannelInitializer<SocketChannel>() {  
                          @Override  
                          protected void initChannel(SocketChannel ch) throws Exception {  
                              ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),new ObjectEncoder(), new ObjectClientHandler(method,args));  
                          }  
                      });  
              
              ChannelFuture f = b.connect("127.0.0.1", 9999).sync();  
              f.channel().closeFuture().sync();  
          } finally {  
              workerGroup.shutdownGracefully();  
          }  
          //I don't have a better way to do this
          //If u have any idea, please contact me
          while(returnObj==null){
          }
          Object result=returnObj;
          returnObj=null;
          return result;
    }
  
    public class ObjectClientHandler extends ChannelInboundHandlerAdapter {  
    	  Method method;
    	  Object[] args;
        public ObjectClientHandler(Method method, Object[] args) {
			this.method=method;
			this.args=args;
		}

		@Override  
        public void channelActive(ChannelHandlerContext ctx) throws Exception {  
            ctx.write(method.getName());  
            ctx.write(method.getParameterTypes());  
            args=new Object[0];
            ctx.write(args);  
            ctx.flush();  
        }  
      
        @Override  
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("Consumer got msg:"+msg);
            returnObj=msg;
//            synchronized (RpcConsumerImpl.this){ 
//          	  RpcConsumerImpl.this.notifyAll();
//            }
            ctx.close();  
        }  
      
        @Override  
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
            cause.printStackTrace();  
            ctx.close();  
        }  
    }
}





























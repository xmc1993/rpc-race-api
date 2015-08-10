package com.alibaba.middleware.race.rpc.server;

import com.alibaba.middleware.race.rpc.model.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import sun.nio.ch.ChannelInputStream;

import java.util.Map;

/**
 * Created by Administrator on 2015/7/30.
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter{

    private final Map<String,Object> handlersMap;

    private final ChannelGroup channelGroup;



    public RpcServerHandler(Map<String, Object> handlersMap) {
        this(handlersMap,null);
    }

    /**
     * 构造函数
     */
    public RpcServerHandler(Map<String, Object> handlersMap,ChannelGroup channelGroup){
        this.handlersMap=handlersMap;
        this.channelGroup=channelGroup;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**如果收到的信息不是RPCRequest则不进行处理*/
        if(!(msg instanceof RpcRequest)){
            return;
        }
        RpcRequest rpcRequest = (RpcRequest)msg;

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

}

package com.alibaba.middleware.race.rpc.api.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2015/7/29.
 */
public class NettyClient {

    private final  String host;
    private final int port;
    private ChannelInitializer<SocketChannel> channel;

    public NettyClient(String host,int port,ChannelInitializer<SocketChannel> channel) {
        this.host = host;
        this.port=port;
        this.channel=channel;
    }

    public void start() throws Exception{
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try{
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host,port))
                    .handler(channel);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

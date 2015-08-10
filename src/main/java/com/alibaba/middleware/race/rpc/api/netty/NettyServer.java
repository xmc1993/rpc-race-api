package com.alibaba.middleware.race.rpc.api.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.Channel;

/**
 * Created by Administrator on 2015/7/29.
 */
public class NettyServer {
    private final int port;
    ChannelInitializer<Channel> channel;


    public NettyServer(int port,ChannelInitializer<Channel> channel) {
        this.port = port;
        this.channel=channel;
    }

    public void start() throws Exception{
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();  //用于管理的线程

        try{
            //创建 bootstrap
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class).localAddress(port)
                    .childHandler(channel);
            serverBootstrap.bind(port);
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}















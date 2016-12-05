package com.valxu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * Kirk Xu
 * 2016-12-04
 */
public class NettyHandlerServer {

    private static void initServer(final int port) throws Exception {

        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        ChannelFuture future = b.group(boosGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new NettyHttpHandlerInitializer())
                .childOption(ChannelOption.AUTO_READ, true)
                .bind(port)
                .sync();
        addRuntimeShutdownHook(boosGroup, workGroup);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Logger logger = Logger.getLogger(NettyHandlerServer.class);
                if (channelFuture.isSuccess()) {
                    logger.info("[Service] Server initialized and listening on port " + port);
                }
                else {
                    logger.error("[Service] Server failed to run");
                }
            }
        }).channel().closeFuture().sync();

    }

    private static void addRuntimeShutdownHook(final EventLoopGroup boosGroup, final EventLoopGroup workGroup) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.getLogger(NettyHandlerServer.class).info("[Service] Shutting down the Server...");
                    boosGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
            }
        }));
    }

    public static void main(String[] args) {
        try {
            initServer(8080);
        } catch (Exception e) {
            Logger.getLogger(NettyHandlerServer.class).error("[Service] Server failed to run: " + e.toString());
            System.exit(1);
        }
    }

}

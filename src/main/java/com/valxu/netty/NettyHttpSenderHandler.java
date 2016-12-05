package com.valxu.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * xuke
 * 2016-12-04
 */
public class NettyHttpSenderHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // send response to client
        ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
    }
}

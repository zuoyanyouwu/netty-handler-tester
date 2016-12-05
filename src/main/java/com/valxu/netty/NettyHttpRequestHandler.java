package com.valxu.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import java.util.Base64;

/**
 * Kirk Xu
 * 2016-12-04
 */
public class NettyHttpRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // update http content to Base64 encode value
        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        ByteBuf contentBuf = httpRequest.content();
        String content = contentBuf.toString(CharsetUtil.UTF_8);
        contentBuf.clear();
        contentBuf.writeBytes(Base64.getEncoder().encode(content.getBytes(CharsetUtil.UTF_8)));
        // update content length
        HttpHeaders.setHeader(httpRequest, HttpHeaders.Names.CONTENT_LENGTH, contentBuf.readableBytes());

        super.channelRead(ctx, msg);
    }
}

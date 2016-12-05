package com.valxu.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import java.util.Base64;

/**
 * Kirk Xu
 * 2016-12-04
 */
public class NettyHttpResponseHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        // update http content to Base64 decode value
        FullHttpResponse response = (FullHttpResponse) msg;
        ByteBuf contentBuf = response.content();
        String content = contentBuf.toString(CharsetUtil.UTF_8);
        contentBuf.clear();
        contentBuf.writeBytes(Base64.getDecoder().decode(content.getBytes(CharsetUtil.UTF_8)));
        // update content length
        HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_LENGTH, contentBuf.readableBytes());

        super.write(ctx, msg, promise);
    }
}

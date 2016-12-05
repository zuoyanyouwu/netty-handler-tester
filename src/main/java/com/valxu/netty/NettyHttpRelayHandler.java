package com.valxu.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Kirk Xu
 * 2016-12-04
 * Relay to NettyHttpResponseHandler
 */
public class NettyHttpRelayHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // convert request to response
        FullHttpRequest request = (FullHttpRequest) msg;
        FullHttpResponse response =
                new DefaultFullHttpResponse(
                        request.getProtocolVersion(),
                        HttpResponseStatus.OK,
                        request.content()
                );
        response.headers().set(request.headers());

        ctx.write(response);
    }
}

package com.valxu.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Kirk Xu
 * 2016-12-04
 */
public class NettyHttpHandlerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        // automatic compress http content
        pipeline.addLast(new HttpContentCompressor());
        // maxContentLength 10M
        pipeline.addLast(new HttpObjectAggregator(10485760));

        pipeline.addLast(new NettyHttpSenderHandler());
        pipeline.addLast(new NettyHttpResponseHandler());

        pipeline.addLast(new NettyHttpRequestHandler());
        pipeline.addLast(new NettyHttpRelayHandler());

    }

}

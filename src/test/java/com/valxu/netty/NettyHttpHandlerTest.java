package com.valxu.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import org.testng.annotations.Test;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Values.DEFLATE;
import static io.netty.handler.codec.http.HttpHeaders.Values.GZIP;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.util.CharsetUtil.UTF_8;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * xuke
 * 2016-12-04
 */
public class NettyHttpHandlerTest {

    private static final String JSON_DATA =
            "{\"data\": \"this is json body\"}";

    @Test
    public void nettyHttpTest() {
        EmbeddedChannel channel = newChannel();
        FullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, POST, "/test");
        setRequestHeaders(request);

        HttpContent chunk = new DefaultHttpContent(Unpooled.wrappedBuffer(JSON_DATA.getBytes(UTF_8)));
        writeInBound(channel, request, chunk);
        // check InBoundHandler processed HttpRequest
        FullHttpRequest reqMsg = (FullHttpRequest) channel.readInbound();
        assertNotNull(reqMsg);

        String body = reqMsg.content().toString(UTF_8);
        assertNotNull(body);
        assertEquals(body, "eyJkYXRhIjogInRoaXMgaXMganNvbiBib2R5In0=");

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.copiedBuffer(body, UTF_8));
        setResponseHeaders(response);

        channel.writeOutbound(response);

        // check OutBoundHandler processed HttpResponse
        FullHttpResponse resMsg = (FullHttpResponse) channel.readOutbound();
        assertNotNull(resMsg);

        body = resMsg.content().toString(UTF_8);
        assertNotNull(body);
        assertEquals(body, JSON_DATA);

        // mark channel as finished
        channel.finish();

    }

    private EmbeddedChannel newChannel() {
        return new EmbeddedChannel(
                new HttpRequestDecoder(),

                new HttpObjectAggregator(10485760),

                new NettyHttpResponseHandler(),

                new NettyHttpRequestHandler()
        );
    }

    private void setRequestHeaders(FullHttpRequest request) {
        HttpHeaders.setHeader(request, CONTENT_TYPE, "application/json");
        HttpHeaders.setHeader(request, ACCEPT, "*/*");
        HttpHeaders.setHeader(request, ACCEPT_ENCODING, GZIP + ',' + DEFLATE);
    }

    private void writeInBound(EmbeddedChannel channel, FullHttpRequest request, HttpContent chunk) {
        HttpHeaders.setHeader(request, CONTENT_LENGTH, chunk.content().readableBytes());
        channel.writeInbound(request);
        channel.writeInbound(chunk);
        channel.writeInbound(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    private void setResponseHeaders(FullHttpResponse response) {
        HttpHeaders.setHeader(response, CONTENT_TYPE, "application/json");
        HttpHeaders.setHeader(response, CONTENT_LENGTH, response.content().readableBytes());
    }

}

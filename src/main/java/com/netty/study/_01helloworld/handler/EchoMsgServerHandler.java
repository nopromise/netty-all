package com.netty.study._01helloworld.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * respond to a request
 * 用telnet 127.0.0.1 8080
 *
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
@Slf4j
public class EchoMsgServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //A ChannelHandlerContext object provides various operations that enable you to
        // trigger various I/O events and operations.
        // Here, we invoke write(Object) to write the received message in verbatim.
        // Please note that we did not release the received message unlike we did in the DISCARD example.
        // It is because Netty releases it for you when it is written out to the wire.
        //ctx.write(Object) does not make the message written out to the wire.
        // It is buffered internally and then flushed out to the wire by ctx.flush().
        // Alternatively, you could call ctx.writeAndFlush(msg) for brevity.

        //If you run the telnet command again,
        // you will see the server sends back whatever you have sent to it.
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
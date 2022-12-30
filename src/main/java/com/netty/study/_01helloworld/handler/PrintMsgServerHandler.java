package com.netty.study._01helloworld.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 打印收到消息的server
 * 用telnet 127.0.0.1 8080
 *
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
@Slf4j
public class PrintMsgServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            //This inefficient loop can actually be simplified to:
            // System.out.println(byteBuf.toString(io.netty.util.CharsetUtil.US_ASCII))
            while (byteBuf.isReadable()) {
                System.out.print((char) byteBuf.readByte());
//                System.out.println(byteBuf.toString(io.netty.util.CharsetUtil.US_ASCII))
                System.out.flush();
            }
        } finally {
            //Alternatively, you could do byteBuf.release() here.
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
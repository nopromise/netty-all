package com.netty.study._02time.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * In a stream-based transport such as TCP/IP,
 * received data is stored into a socket receive buffer.
 * Unfortunately, the buffer of a stream-based transport is not a queue of packets
 * but a queue of bytes.
 * It means, even if you sent two messages as two independent packets,
 * an operating system will not treat them as two messages but as just a bunch of bytes.
 * Therefore, there is no guarantee that what you read is exactly what your remote peer wrote.
 * For example, let us assume that the TCP/IP stack of an operating system has received three packets
 * https://netty.io/wiki/user-guide-for-4.x.html
 *
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
public class SafeTimeClientHandler extends ChannelInboundHandlerAdapter {
    //Now let us get back to the TIME client example. We have the same problem here.
    // A 32-bit integer is a very small amount of data, and it is not likely to be fragmented often.
    // However, the problem is that it can be fragmented,
    // and the possibility of fragmentation will increase as the traffic increases.
    //
    //The simplistic solution is to create an internal cumulative buffer
    // and wait until all 4 bytes are received into the internal buffer.
    // The following is the modified TimeClientHandler implementation that fixes the problem:
    //
    //
    private ByteBuf buf;

    //A ChannelHandler has two life cycle listener methods:
    // handlerAdded() and handlerRemoved().
    // You can perform an arbitrary (de)initialization task as long as it does not block for a long time.
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4); // (1)
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release(); // (1)
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //First, all received data should be cumulated into buf.
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m); // (2)
        m.release();

        //And then, the handler must check if buf has enough data,
        //4 bytes in this example,
        // and proceed to the actual business logic.
        // Otherwise, Netty will call the channelRead() method again when more data arrives,
        // and eventually all 4 bytes will be cumulated.
        if (buf.readableBytes() >= 4) { // (3)
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
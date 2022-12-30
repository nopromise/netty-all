package com.netty.study._02time_pojo.handler;

import com.netty.study._02time_pojo.entity.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    //编码，对象到字节
    //Now, the only missing piece is an encoder,
    // which is an implementation of ChannelOutboundHandler that translates a UnixTime back into a ByteBuf.
    // It's much simpler than writing a decoder
    // because there's no need to deal with packet fragmentation and assembly when encoding a message.
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime time = (UnixTime) msg;
        ByteBuf byteBuf = ctx.alloc().buffer(4);
        //编码，写出
        byteBuf.writeInt((int) time.value());
        ctx.write(byteBuf, promise); // (1)
        //First, we pass the original ChannelPromise as-is
        // so that Netty marks it as success or failure
        // when the encoded data is actually written out to the wire.
        //Second, we did not call ctx.flush().
        // There is a separate handler method void flush(ChannelHandlerContext ctx)
        // which is purposed to override the flush() operation.
    }
}
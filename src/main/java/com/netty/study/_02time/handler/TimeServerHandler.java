package com.netty.study._02time.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    //As explained, the channelActive() method will be invoked when a connection is established and ready to generate traffic.
    // Let's write a 32-bit integer that represents the current time in this method.
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //To send a new message, we need to allocate a new buffer which will contain the message.
        // We are going to write a 32-bit integer, and therefore we need a ByteBuf whose capacity is at least 4 bytes.
        // Get the current ByteBufAllocator via ChannelHandlerContext.alloc() and allocate a new buffer.
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        //ch.close();// the code might close the connection even before a message is sent,because all operations are asynchronous in Netty
        f.addListener(new ChannelFutureListener() {
            //Therefore, you need to call the close() method after the ChannelFuture is complete,
            // which was returned by the write() method,
            // and it notifies its listeners when the write operation has been done.
            // Please note that, close() also might not close the connection immediately,
            // and it returns a ChannelFuture.
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();//closes the Channel when the operation is done.
            }
        }); // (4)
        //How do we get notified when a write request is finished then?
        // This is as simple as adding a ChannelFutureListener to the returned ChannelFuture.
        // Here, we created a new anonymous ChannelFutureListener which closes the Channel when the operation is done.
        //Alternatively, you could simplify the code using a pre-defined listener:
        //f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
package com.netty.study._02time_pojo.handler;

import com.netty.study._02time_pojo.entity.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
public class TimeEncoder2 extends MessageToByteEncoder<UnixTime> {
    //To simplify even further, you can make use of MessageToByteEncoder:
    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
        out.writeInt((int)msg.value());
    }
}
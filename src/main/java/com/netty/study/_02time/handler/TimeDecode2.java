package com.netty.study._02time.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
public class TimeDecode2  extends ReplayingDecoder<Void> {
    //If you are an adventurous person,
    // you might want to try the ReplayingDecoder which simplifies the decoder even more.
    // You will need to consult the API reference for more information though.
    @Override
    protected void decode(
            ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(in.readBytes(4));
    }
}
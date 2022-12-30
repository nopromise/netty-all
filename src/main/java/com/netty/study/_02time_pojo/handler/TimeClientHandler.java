package com.netty.study._02time_pojo.handler;

import com.netty.study._02time_pojo.entity.UnixTime;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //TODO 感觉不对，服务的写出的是int 类型的time
        //读取到的数据
        UnixTime m = (UnixTime) msg;
        //打印到本地
        System.out.println(m);
        log.info("收到时间：{}", m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
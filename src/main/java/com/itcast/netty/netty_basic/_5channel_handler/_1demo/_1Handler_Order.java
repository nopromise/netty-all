package com.itcast.netty.netty_basic._5channel_handler._1demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _1Handler_Order {
    public static void main(String[] args) {
        try {
            new ServerBootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 1
                     * 2
                     * 3
                     * 6
                     * 5
                     * 4
                     */
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel ch) {
                            log.info("init channel........");
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    System.out.println(1);
                                    ctx.fireChannelRead(msg); // 1
                                }
                            });
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    System.out.println(2);
                                    //TODO 调用下一个ChannelInboundHandlerAdapter
                                    ctx.fireChannelRead(msg); // 2
                                }
                            });
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    System.out.println(3);
                                    //TODO 不执行write操作，不会进入到ChannelOutboundHandlerAdapter
                                    ctx.channel().write(msg); // 1-2-3-6-5-4
                                    //TODO 这里是ctx.write，就是从当前这个位置write，不会从最后一个handler
                                    // ctx.write(msg);
                                    // ctx.write(msg); // 1-2-3
                                }
                            });
                            ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg,
                                                  ChannelPromise promise) {
                                    System.out.println(4);
                                    ctx.write(msg, promise); // 4
                                }
                            });
                            ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg,
                                                  ChannelPromise promise) {
                                    System.out.println(5);
                                    ctx.write(msg, promise); // 5
                                }
                            });
                            ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg,
                                                  ChannelPromise promise) {
                                    System.out.println(6);
                                    ctx.write(msg, promise); // 6
                                }
                            });
                        }
                    })
                    .bind(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

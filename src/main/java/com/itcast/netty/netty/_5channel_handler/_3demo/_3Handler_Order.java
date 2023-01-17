package com.itcast.netty.netty._5channel_handler._3demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _3Handler_Order {
    /**
     * bug
     * 1
     * 2
     * 3
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     * 5
     * 6
     *
     * @param args
     */
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
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
                                //将数据传入到下个handler，如果没有，调用链会断开。
                                ctx.fireChannelRead(msg); // 2
                                //super.channelRead(ctx,msg);

                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(3);
                                //TODO 不执行write操作，不会进入到ChannelOutboundHandlerAdapter
                                //TODO 这里是ctx.channel().write(msg)，从最后一个handler来write
                                ctx.channel().write(msg); // 3
                                //TODO 这里是ctx.write，就是从当前这个位置write，不会从最后一个handler
//                                ctx.write(msg);
                                log.info("this--3--write");
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
//                                ctx.write(msg, promise); // 5
                                //TODO 这样？会存在bug！！
                                ctx.channel().write(msg);
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
    }
}

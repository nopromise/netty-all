package com.itcast.netty.netty_basic._2eventLoop._2demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-12
 */
@Slf4j
public class MyServer {
    /**
     * eventLoop与channel绑定，处理io事件任务
     *
     * @param args
     */
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        log.info("init channel...");
                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                try {
                                    ByteBuf buf = (ByteBuf) msg;
                                    log.info("---threadName:{}---content:{}",Thread.currentThread().getName(),buf.toString(StandardCharsets.UTF_8));
//                                System.out.println(Thread.currentThread().getName() + " "
//                                        + buf.toString(StandardCharsets.UTF_8));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                })
                .bind(8080);
    }
}
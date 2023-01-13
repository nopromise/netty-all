package com.itcast.netty.netty._2EventLoop.demo4_customize;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-13
 */
public class MyServer {
    /**
     * 增加自定义EventLoopGroup
     * 当有的任务需要较长的时间处理时，可以使用非NioEventLoopGroup，避免同一个NioEventLoop中的其他Channel在较长的时间内都无法得到处理
     *
     * 启动四个客户端发送数据
     * nioEventLoopGroup-4-1 hello1
     * defaultEventLoopGroup-2-1 hello1
     * nioEventLoopGroup-4-2 hello2
     * defaultEventLoopGroup-2-2 hello2
     * nioEventLoopGroup-4-1 hello3
     * defaultEventLoopGroup-2-3 hello3
     * nioEventLoopGroup-4-2 hello4
     * defaultEventLoopGroup-2-4 hello4
     *
     * @param args
     */
    public static void main(String[] args) {
        // 增加自定义的非NioEventLoopGroup
        EventLoopGroup group = new DefaultEventLoopGroup();

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 增加两个handler，第一个使用NioEventLoopGroup处理，第二个使用自定义EventLoopGroup处理
                        socketChannel.pipeline().addLast("nioHandler", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println(Thread.currentThread().getName() + " " + buf.toString(StandardCharsets.UTF_8));
                                        // 调用下一个handler
                                        ctx.fireChannelRead(msg);
                                    }
                                })
                                // 该handler绑定自定义的Group
                                .addLast(group, "myHandler", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println(Thread.currentThread().getName() + " " + buf.toString(StandardCharsets.UTF_8));
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
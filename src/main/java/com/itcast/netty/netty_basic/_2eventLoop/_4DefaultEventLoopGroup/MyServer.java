package com.itcast.netty.netty_basic._2eventLoop._4DefaultEventLoopGroup;

import com.itcast.netty.util.LogUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-13
 */
@Slf4j
public class MyServer {
    /**
     * 增加自定义EventLoopGroup
     * 当有的任务需要较长的时间处理时，可以使用非NioEventLoopGroup，避免同一个NioEventLoop中的其他Channel在较长的时间内都无法得到处理
     * <p>
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
        //某个handler如果耗时较长的话，使用自定义的DefaultEventLoopGroup，不要阻塞NioEventLoopGroup的io事件。
        //创建新的独立的EventLoopGroup，不用NioEventLoopGroup处理耗时较长的事件。
        /**
         * 11:44:03.599 [nioEventLoopGroup-4-1] INFO com.itcast.netty.util.LogUtils - 线程:nioEventLoopGroup-4-1,内容:hello1
         * 11:44:03.600 [defaultEventLoopGroup-2-1] INFO com.itcast.netty.util.LogUtils - 线程:defaultEventLoopGroup-2-1,内容:hello1
         * 11:44:08.176 [nioEventLoopGroup-4-1] INFO com.itcast.netty.util.LogUtils - 线程:nioEventLoopGroup-4-1,内容:北京
         * 11:44:08.176 [defaultEventLoopGroup-2-1] INFO com.itcast.netty.util.LogUtils - 线程:defaultEventLoopGroup-2-1,内容:北京
         */
        EventLoopGroup group = new DefaultEventLoopGroup();

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 增加两个handler，第一个使用NioEventLoopGroup处理，第二个使用自定义EventLoopGroup处理
                        socketChannel.pipeline()
                                .addLast("nioHandler", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        LogUtils.logThreadAndContent(Thread.currentThread().getName(), buf.toString(StandardCharsets.UTF_8));
                                        // 自定义handler时，调用下一个handler，不执行fireChannelRead，消息到了这个handler就断了，
                                        //不会执行到下面的myHandler了。
                                        //TODO 2个handler，都会去执行
                                        ctx.fireChannelRead(msg);
                                    }
                                })
                                // 该handler绑定自定义的Group
                                .addLast(group, "myHandler", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
//                                        System.out.println(Thread.currentThread().getName() + " " + buf.toString(StandardCharsets.UTF_8));
                                        LogUtils.logThreadAndContent(Thread.currentThread().getName(), buf.toString(StandardCharsets.UTF_8));
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
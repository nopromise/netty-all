package com.itcast.netty.netty._3channel._1connectProblem;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-13
 */
public class _1MyClient_sync {
    /**
     * 如果我们去掉channelFuture.sync()方法，会服务器无法收到hello world
     *
     * 这是因为建立连接(connect)的过程是异步非阻塞的，若不通过sync()方法阻塞主线程，等待连接真正建立，
     * 这时通过 channelFuture.channel() 拿到的 Channel 对象，
     * 并不是真正与服务器建立好连接的 Channel，也就没法将信息正确的传输给服务器
     * 所以需要通过channelFuture.sync()方法，阻塞主线程，同步处理结果，等待连接真正建立好以后，
     * 再去获得 Channel 传递数据。使用该方法，获取 Channel 和发送数据的线程都是主线程
     *
     * 下面还有一种方法，用于异步获取建立连接后的 Channel 和发送数据，使得执行这些操作的线程是 NIO 线程（去执行connect操作的线程）
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 该方法为异步非阻塞方法，主线程调用后不会被阻塞，真正去执行连接操作的是NIO线程
                // NIO线程：NioEventLoop 中的线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 该方法用于等待连接真正建立
        //如果我们去掉channelFuture.sync()方法，会服务器无法收到hello world
        channelFuture.sync();

        // 获取客户端-服务器之间的Channel对象
        Channel channel = channelFuture.channel();
        channel.writeAndFlush("hello world");
        System.in.read();
    }
}
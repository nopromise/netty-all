package com.netty.study._02time_pojo;

import com.netty.study._02time_pojo.handler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //Bootstrap is similar to ServerBootstrap except that
            // it's for non-server channels such as a client-side or connectionless channel.
            Bootstrap b = new Bootstrap(); // (1)
            //If you specify only one EventLoopGroup,
            // it will be used both as a boss group and as a worker group.
            // The boss worker is not used for the client side though.
            b.group(workerGroup); // (2)
            //Instead of NioServerSocketChannel, NioSocketChannel is being used to create a client-side Channel.
            b.channel(NioSocketChannel.class); // (3)
            //Note that we do not use childOption() here unlike we did with ServerBootstrap
            // because the client-side SocketChannel does not have a parent.
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    //单独SimpleTimeClientHandler，可能不到完整的数据包
//                    ch.pipeline().addLast(new SimpleTimeClientHandler());
                    //能接收到完整的数据包
//                    ch.pipeline().addLast(new SafeTimeClientHandler());
                    //优雅，配合使用，拆分成1个解码（ByteToMessageDecoder），一个处理业务
                    ch.pipeline().addLast(new TimeClientHandler());
                    //Additionally, Netty provides out-of-the-box decoders
                    // which enables you to implement most protocols very easily and helps you avoid from ending up with a monolithic unmaintainable handler implementation.
                    // Please refer to the following packages for more detailed examples:
//                    ch.pipeline().addLast(new TimeDecode2());
                }
            });

            //We should call the connect() method instead of the bind() method.
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
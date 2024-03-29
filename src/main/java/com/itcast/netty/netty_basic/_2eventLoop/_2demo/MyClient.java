package com.itcast.netty.netty_basic._2eventLoop._2demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-12
 */
public class MyClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080))
                .sync()
                .channel();
        System.out.println(channel);
        // 此处打断点调试，调用 channel.writeAndFlush(...);
        //使用evaluate
        try {
            //TODO 报错：unsupported message type: HeapByteBuffer (expected: ByteBuf, FileRegion)
            //因为已经添加了StringEncoder这个handler，就不需要再进行编码了
//            channel.writeAndFlush(Charset.defaultCharset().encode("hihallo"));
            channel.writeAndFlush("hihallo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}
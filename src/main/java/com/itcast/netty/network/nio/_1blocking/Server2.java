package com.itcast.netty.network.nio._1blocking;


import com.itcast.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server2 {
    /**
     * 阻塞模式-单线程
     * 阻塞模式
     * 阻塞模式下，单线程不能很好的工作，1个方法的调用会影响到另一个方法的调用，因为阻塞！
     * 如何解决：
     * 阻塞模式下，建立一个连接，就再创建一个线程
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        //
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //2.监听绑定端口
        ssc.bind(new InetSocketAddress(8080));
        //3.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //4.accept建立与客户端的连接,SocketChannel用来与客户端通信
            log.debug("wait connect...");
            //阻塞的方法
            SocketChannel sc = ssc.accept();
            log.debug("connected...{}", sc);
            channels.add(sc);
            for (SocketChannel channel : channels) {
                //5.接收客户端发送的数据
                log.debug("before read...{}", channel);
                threadPool.submit(() -> {
                    //阻塞方法，客户端发数据后
                    try {
                        channel.read(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...,{}", channel);
                });

            }
        }
    }
}












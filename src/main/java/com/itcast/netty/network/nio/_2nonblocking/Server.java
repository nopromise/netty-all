package com.itcast.netty.network.nio._2nonblocking;


import com.itcast.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Server {
    /**
     * 非阻塞模式
     * <p>
     * 线程一直在循环，空的转
     * <p>
     * 建立连接和读取数据都非阻塞的
     * ServerSocketChannel#configureBlocking(false);
     * SocketChannel#configureBlocking(false);
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        //
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //非阻塞模式
        ssc.configureBlocking(false);
        //2.监听绑定端口
        ssc.bind(new InetSocketAddress(8080));
        //3.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //4.accept建立与客户端的连接,SocketChannel用来与客户端通信
            //非阻塞，线程继续运行，如果没有连接建立，返回null
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected...{}", sc);
                //socketChannel也设置成非阻塞
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                //5.接收客户端发送的数据
                //非阻塞方法，如果没有读取到数据，read返回0
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...,{}", channel);
                }

            }
        }
    }
}












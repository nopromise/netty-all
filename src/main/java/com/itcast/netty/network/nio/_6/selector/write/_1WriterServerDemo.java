package com.itcast.netty.network.nio._6.selector.write;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 模拟服务器写不完的情况
 */
@Slf4j
public class _1WriterServerDemo {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                //用完，立即异常key
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 1. 向客户端发送内容
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 300000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    //数据太多的时间，一次性写不完！
                    //缓冲区满了，socketChannel满了？
                    //不是channel，是操作系统有个缓冲区，是从这个socket缓冲区专门发送到网络对端去的
                    //TODO 虽然能把数据发送给客户端，但是一直卡在while循环这里，操作系统、网络等影响，不是每次循环都发送数据的
                    //TODO 会影响其他事件的执行，浪费cpu执行时间
                    //TODO 这时候可以处理别的操作去
                    while (buffer.hasRemaining()) {
                        // 2. write 表示实际写了多少字节
                        int write = sc.write(buffer);
                        System.out.println("实际写入字节:" + write);
                    }
                    /**
                     * 实际写入字节:261676
                     * 实际写入字节:15629612
                     * 实际写入字节:6996048
                     * 实际写入字节:15486680
                     * 实际写入字节:8210388
                     * 实际写入字节:11535040
                     * 实际写入字节:40489812
                     * 实际写入字节:65406552
                     * 实际写入字节:7386064
                     * 实际写入字节:6551468
                     * 实际写入字节:10896896
                     * 实际写入字节:36928500
                     * 实际写入字节:11430468
                     * 实际写入字节:14677568
                     * 实际写入字节:12416420
                     * 实际写入字节:10449924
                     * 实际写入字节:21181028
                     * 实际写入字节:2090496
                     * 实际写入字节:0
                     * 实际写入字节:1975360
                     */
                }
            }
        }
    }
}

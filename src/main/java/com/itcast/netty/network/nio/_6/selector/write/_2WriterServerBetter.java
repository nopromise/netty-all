package com.itcast.netty.network.nio._6.selector.write;

import io.netty.channel.ServerChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 模拟服务器写不完,使用写事件
 */
@Slf4j
public class _2WriterServerBetter {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        //serverSocketChannel注册到selector后获取到的key
        SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

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
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);

                    // 1. 向客户端发送内容
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 300000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    //数据太多的时间，一次性写不完！
                    // 2. write 表示实际写了多少字节
                    int write = sc.write(buffer);
                    System.out.println("实际写入字节:" + write);
                    //3. 判断是否有剩余内容
                    if (buffer.hasRemaining()) {
                        //4.关注可写事件
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
//                        sscKey.interestOps(sscKey.interestOps() | SelectionKey.OP_WRITE);
                        //5.把未写完的数据挂到sscKey上
                        scKey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println("实际写入字节:" + write);
                    //6.清理工作
                    //洗完后清理
                    if (!buffer.hasRemaining()) {
                        //释放bytebuffer的内存，清楚buffer
                        key.attach(null);
                        //不再关注可写事件
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}

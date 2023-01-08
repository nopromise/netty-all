package com.itcast.netty.network.nio._4selector_read_remove;


import com.itcast.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ServerForSelectorRead {
    /**
     * selector
     * 1.处理read事件
     * 2. 用完key后要remove！！！
     * 3.把客户端关闭，服务器server报异常 IOException：远程主机强制关闭了一个现有的连接
     * 。而MAC不会报异常，read会返回-1{m1版本的mac，用zulu，jdk不会抛出异常}
     * 客户端关闭后，出发一个read事件
     * <p>
     * 正常断开：
     * sc.close()方法
     * 可以看一下tcp协议，一般这种强制断开会发送RST的报错报文
     * <p>
     * 不管正常断开(sc.close())还是非正常断开(点击关闭按钮,mac也返回-1，其他报异常)
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        //1.创建selector,管理多个Channel{ServerSocketChannel、SocketChannel}
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //2.把channel注册到selector
        //SelectionKey 就是将来事件发生后，通过它可以知道事件和哪个channel的事件
        //事件的类型：
        // accept、有连接请求时候处罚
        // connect、客户端，连接建立后
        // read、可读事件
        // write、可写事件
        SelectionKey sscKey = ssc.register(selector, 0, null);//0表示不关注任何时间
        //sscKey只关注accept时间
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", sscKey);
        ssc.bind(new InetSocketAddress(8080));
//        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //3. select 方法，没有事件发生，阻塞。有事件发送，线程才恢复运行
            selector.select();
            //4.处理事件,selectionKeys包含了所有发送的事件,用迭代器遍历
            //
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //一定要remove！
                iterator.remove();
                log.debug("key:{}", key);
                //5.区分事件类型
                if (key.isAcceptable()) {
                    log.debug("enter accept");
                    //连接事件
                    SelectableChannel selectableChannel = key.channel();
                    ServerSocketChannel channel = (ServerSocketChannel) selectableChannel;
                    //建立连接
                    SocketChannel sc = channel.accept();
                    //********设置为非阻塞*********
                    sc.configureBlocking(false);
                    //建立连接后，注册sc到selector，关注读事件
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    log.debug("enter read");
                    //读事件
                    key.channel();
                    SelectableChannel selectableChannel = key.channel();
                    SocketChannel channel = (SocketChannel) selectableChannel;
                    //
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    int read = channel.read(buffer);
                    //The number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream
                    if (read == -1) {
                        //客户端断开(sc.close或者非正常断开)
                        log.warn("客户端断开连接，客户端：{}", channel.getRemoteAddress());
                        //可能是客户端断开了,需要把key取消掉
                        key.cancel();
                    } else {
                        buffer.flip();
                        ByteBufferUtil.debugRead(buffer);
                    }
                }

            }

        }
    }
}












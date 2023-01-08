package com.itcast.netty.network.nio._3selector_accept_cancel;


import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
public class ServerForSelectorCancel {
    /**
     * selector
     * 1.把channel注册到selector上，并设置关注的事件
     * 2.selector.select();是阻塞方法，有事件发生时才继续往下运行
     * 3.Set<SelectionKey> selectionKeys = selector.selectedKeys(); 遍历keys，通过key获取发送该事件的channel
     * <p>
     * ##
     * SelectionKey sscKey = ssc.register(selector, 0, null);//0表示不关注任何时间
     * sscKey.interestOps(SelectionKey.OP_ACCEPT);
     * 多个channel注册到这个selector上，sscKey是同一个key，channel是不同channel
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
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            //3. select 方法，没有事件发生，阻塞。有事件发送，线程才恢复运行
            selector.select();
            //4.处理事件,selectionKeys包含 了所有发送的事件,用迭代器遍历
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                log.debug("key:{}", key);
                //如果事件不进行处理（channel.accept();），会一直循环执行
                //可以调用 cancel方法取消事件
                key.cancel();
/*                SelectableChannel selectableChannel = key.channel();
                ServerSocketChannel channel = (ServerSocketChannel) selectableChannel;
                //建立连接
                SocketChannel sc = channel.accept();
                log.debug("establish connection,sc:{}", sc);*/

            }

        }
    }
}












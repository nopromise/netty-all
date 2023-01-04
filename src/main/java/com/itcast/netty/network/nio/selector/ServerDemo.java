package com.itcast.netty.network.nio.selector;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public class ServerDemo {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //非阻塞模式
        ssc.configureBlocking(false);
        //2 bind port


    }
}

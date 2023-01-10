package com.itcast.netty.network.nio._7multithread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

@Slf4j
public class MultiThreadServerClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
//        sc.write(Charset.defaultCharset().encode("123456789abcdefghijklmn\nhello\nwelcome\n"));
        sc.write(Charset.defaultCharset().encode("12345"));
        //阻塞住
        System.in.read();
    }
}

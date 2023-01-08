package com.itcast.netty.network.nio._2nonblocking;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        //1.打断点，停在这里
        //2.然后使用debug的evaluate功能给服务端发送数据,查看接收端是否收到
        //sc.write(Charset.defaultCharset().encode("hehe"));
        log.debug("waiting...");
    }
}

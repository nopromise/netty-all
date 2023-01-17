package com.itcast.netty.network.nio._7multithread;

import com.itcast.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
public class _1MultiThreadOneWorkerWorseServer {
    /**
     * boss负责连接
     * worker负责处理读写等
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //boss selector
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //1.创建固定数量的worker
        Worker worker = new Worker("worker-0");


        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                //用完，立即异常key
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    //2. 关联到worker的selector 上
                    log.debug("connected...{}", sc.getRemoteAddress());
                    log.debug("before register...{}", sc.getRemoteAddress());
                    //注册，并启动
                    worker.register(sc);
                    //注册读事件到selector上
                    sc.register(worker.selector, SelectionKey.OP_READ, null);
                    log.debug("after register...{}", sc.getRemoteAddress());
                }
            }
        }
    }


    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean start = false;
        //在2个线程之间传递数据，用队里
        private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();

        public Worker(String name) {
            this.name = name;
        }

        /**
         * Worker的注册register和Worker的run是2个线程，一个线程只注册一次。
         * register
         * run
         *
         * @param sc
         * @throws IOException
         */
        //初始化线程和selector
        //保证只执行一遍
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                selector = Selector.open();
                //因为实现实现了runnable接口
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.debug("worker thread blocking...");
                    selector.select();//worker0 线程
                    log.debug("worker thread running...");

                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("read...{}", channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

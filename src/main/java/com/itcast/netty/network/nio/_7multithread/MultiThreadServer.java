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
public class MultiThreadServer {
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
//                    sc.register(worker.selector, SelectionKey.OP_READ, null);
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
            //boss线程中执行的
//            sc.register(selector, SelectionKey.OP_READ, null);
            //向队列添加了任务，但是任务并没有被立刻执行
            //nacos这么做的
            //netty这么做的
            //队列，线程间通信？
            //添加一个runnable任务到队列
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            //TODO 秒啊
            //唤醒run方法中的阻塞
            selector.wakeup();
            log.debug("wakeup worker...");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.debug("worker thread blocking...");
                    selector.select();//worker0 线程
                    log.debug("worker thread running...");
                    Runnable task = queue.poll();
                    if (task != null) {
                        //同步的，执行了注册selector的方法，worker0 线程中
                        task.run();
                    }

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
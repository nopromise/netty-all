package com.itcast.netty.netty._2eventLoop._1test;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-12
 */
@Slf4j
public class TestEventLoop {
    /**
     * EventLoop不与channel绑定，执行普通任务
     * @param args
     */
    public static void main(String[] args) {
        // 创建拥有两个EventLoop的NioEventLoopGroup，对应两个线程
        EventLoopGroup group = new NioEventLoopGroup(2);
        //DefaultEventLoopGroup 只处理普通任务，不处理io任务。
//        EventLoopGroup group = new DefaultEventLoopGroup(2);
        // 通过next方法可以获得下一个 EventLoop
        System.out.println(NettyRuntime.availableProcessors());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //执行普通任务，没有与channel绑定
        // 通过EventLoop执行普通任务
        group.next().execute(()->{
            System.out.println(Thread.currentThread().getName() + " hello");
        });

        //通过EventLoop执行定时任务
        group.next().scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName() + " hello2");
        }, 0, 1, TimeUnit.SECONDS);

        // 优雅地关闭
        group.shutdownGracefully();
    }
}
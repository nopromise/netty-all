package com.itcast.netty.netty_basic._7bytebuf;

import com.itcast.netty.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class _1TestByteBuf {
    /**
     * 直接内存创建和销毁的代价昂贵，但读写性能高（少一次内存复制），适合配合池化功能一起用
     * 直接内存对 GC 压力小，因为这部分内存不受 JVM 垃圾回收的管理，但也要注意及时主动释放
     * @param args
     */
    public static void main(String[] args) {
        //默认的 ByteBuf（池化基于直接内存的 ByteBuf）
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        ByteBuf byteBuf2 = ByteBufAllocator.DEFAULT.buffer(10,1024);
        ByteBufUtil.log(byteBuf);
        //可以使用下面的代码来创建池化基于堆的 ByteBuf
//        ByteBuf buffer = ByteBufAllocator.DEFAULT.heapBuffer(10);
        //也可以使用下面的代码来创建池化基于直接内存的 ByteBuf
//        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(10);

    }
}

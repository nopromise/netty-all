package com.itcast.netty.netty_basic._7bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _4TestByteBufSlice {

    /**
     * 【零拷贝】的体现之一，对原始 ByteBuf 进行切片成多个 ByteBuf，
     * 切片后的 ByteBuf 并没有发生内存复制，还是使用原始 ByteBuf 的内存，
     * 切片后的 ByteBuf 维护独立的 read，write 指针
     *
     * @param args
     */
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{1, 2, 3, 4});
        origin.readByte();
        log.info("origin");
        com.itcast.netty.util.ByteBufUtil.log(origin);
        //这时调用 slice 进行切片，
        // 无参 slice 是从原始 ByteBuf 的 read index 到 write index 之间的内容进行切片，
        // 切片后的 max capacity 被固定为这个区间的大小，因此不能追加 write
        //执行slice操作后，元素origin和操作后的slice的2个buf互不影响。都维护各自的读写指针。
        ByteBuf slice = origin.slice();
        log.info("slice");
        com.itcast.netty.util.ByteBufUtil.log(slice);

        // slice.writeByte(5); 如果执行，会报 IndexOutOfBoundsException 异常
        //如果原始 ByteBuf 再次读操作（又读了一个字节）
        origin.readByte();
        log.info("origin");
        com.itcast.netty.util.ByteBufUtil.log(origin);
        //这时的 slice 不受影响，因为它有独立的读写指针
        log.info("slice");
        com.itcast.netty.util.ByteBufUtil.log(slice);
    }
}

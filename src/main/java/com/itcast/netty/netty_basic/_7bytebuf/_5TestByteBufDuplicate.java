package com.itcast.netty.netty_basic._7bytebuf;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _5TestByteBufDuplicate {

    /**
     * 【零拷贝】的体现之一，就好比截取了原始 ByteBuf 所有内容，
     * 并且没有 max capacity 的限制，也是与原始 ByteBuf 使用同一块底层内存，
     * 只是读写指针是独立的
     *
     * @param args
     */
    public static void main(String[] args) {
    }
}

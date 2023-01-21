package com.itcast.netty.netty_basic._7bytebuf;

import com.itcast.netty.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _9TestSlice_Retain {

    /**
     * slice后，自己retain一下，自己release。
     * 不自己tetain，别人release后，自己就没法继续用了
     *
     * @param args
     */
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        ByteBufUtil.log(buf);
        ByteBuf buf1 = buf.slice(0, 5);
        ByteBufUtil.log(buf1);
        //TODO Increases the reference count by 1.
        buf1.retain();
        //释放
        buf.release();
        ByteBufUtil.log(buf1);
        //
        buf1.release();
    }
}

package com.itcast.netty.netty_basic._7bytebuf;

import com.itcast.netty.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class _2TestByteBufWriteRead {

    /**
     * 扩容规则是(好像不一致)
     * <p>
     * 如何写入后数据大小未超过 512，则选择下一个 16 的整数倍，例如写入后大小为 12 ，则扩容后 capacity 是 16
     * 如果写入后数据大小超过 512，则选择下一个 2^n，例如写入后大小为 513，则扩容后 capacity 是 2^10=1024（2^9=512 已经不够了）
     * 扩容不能超过 max capacity 会报错
     *
     * @param args
     */
    public static void main(String[] args) {
        //默认的 ByteBuf（池化基于直接内存的 ByteBuf）
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        ByteBufUtil.log(byteBuf);

        //========写入===========
        //先写入 4 个字节
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBufUtil.log(byteBuf);
        //再写入一个 int 整数，也是 4 个字节
        byteBuf.writeInt(5);
        ByteBufUtil.log(byteBuf);
        //还有一类方法是 set 开头的一系列方法，也可以写入数据，但不会改变写指针位置
        byteBuf.setInt(4, 6);
        ByteBufUtil.log(byteBuf);
        //再写入一个 int 整数时，容量不够了（初始容量是 10），这时会引发扩容
        byteBuf.writeInt(7);
        ByteBufUtil.log(byteBuf);

        //========读取===========
        //读过的内容，就属于废弃部分了，再读只能读那些尚未读取的部分
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());
        ByteBufUtil.log(byteBuf);
        //如果需要重复读取 int 整数 5，怎么办？
        //可以在 read 前先做个标记 mark
        byteBuf.markReaderIndex();
        System.out.println("读到的数据：" + byteBuf.readInt());
        ByteBufUtil.log(byteBuf);
        //这时要重复读取的话，重置到标记位置 reset
        byteBuf.resetReaderIndex();
        System.out.println("resetReaderIndex...");
        ByteBufUtil.log(byteBuf);
        //还有种办法是采用 get 开头的一系列方法，这些方法不会改变 read index
        System.out.println("读到的数据：" + byteBuf.getInt(4));
        ByteBufUtil.log(byteBuf);
    }
}

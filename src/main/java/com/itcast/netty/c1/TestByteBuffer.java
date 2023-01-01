package com.itcast.netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        //ByteBuffer 正确使用姿势
        //1.向buffe写入数据，如调用channel.read(buffer);
        //2.调flap()切换到读模式
        //3.从buffer读数据，如 byte b = buffer.get();
        //4.调clear()或者compact()切换到写模式
        //5.重复1-4步骤
        try {
            //获取FileChannel 1.输入输出流；2.RandomAccessFile
            FileChannel channel = new FileInputStream("src/main/1.txt").getChannel();
            //缓冲区 分配10字节
            //position=0 limit=10 capacity=10
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                //每次读1个字节
                //从channel中读取数据，写入到buffer
                int len = channel.read(buffer);
                log.debug("读取到的字节数{}", len);
                if (len == -1) {
                    break;
                }
                //
                buffer.flip();
                //position<limit
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    log.debug("读取到的字节{}", (char) b);
                }
                //position=0; limit=capacity
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

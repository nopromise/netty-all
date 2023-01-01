package example.netty.demo.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
@Slf4j
public class ByteBufDiscardReadBytesDemo {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.directBuffer(10);
        String msg = "hello";
        log.info("写入hello到buf");
        buf.writeBytes(msg.getBytes());
        //从buf中读取一个字节
        byte b = buf.readByte();
        log.info("从buf中读取1个字节{}", b);
        printBuffer(buf);
        //把读过的字节丢掉
        buf.discardReadBytes();
        log.info("调用discardReadBytes");
        printBuffer(buf);
        //
        byte b1 = buf.readByte();
        log.info("从buf中读取1个字节{}", b1);
        printBuffer(buf);
    }

    /**
     * 打印出ByteBuf的信息
     *
     * @param buffer
     */
    private static void printBuffer(ByteBuf buffer) {
        System.out.println("readerIndex：" + buffer.readerIndex());
        System.out.println("writerIndex：" + buffer.writerIndex());
        System.out.println("capacity：" + buffer.capacity());
    }
}
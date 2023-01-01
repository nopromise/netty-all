package example.netty.demo.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fjl
 * @CreateTime: 2022-12-30
 */
@Slf4j
public class ByteBufClearDemo {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.directBuffer(10);
        String msg = "hello";
        log.info("写入hello到buf");
        buf.writeBytes(msg.getBytes());
        //从buf中读取一个字节
        byte b = buf.readByte();
        log.info("从buf中读取1个字节{}", b);
        printBuffer(buf);
        //clear
        buf.clear();
        log.info("调用clear");
        printBuffer(buf);

        //
        String msg2 = "bye";
        log.info("写入bye到buf");
        buf.writeBytes(msg2.getBytes());
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
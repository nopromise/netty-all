/**
 * Welcome to https://waylau.com
 */
package example.netty.demo.buffer.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBuf with Heap Buffer Mode Demo.
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 1.0.0 2019年10月7日
 */
public class ByteBufHeapBufferDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // 创建一个堆缓冲区
        ByteBuf buffer = Unpooled.buffer(10);
        String s = "example";
        buffer.writeBytes(s.getBytes());

        // 检查是否是支撑数组
        if (buffer.hasArray()) {
            //堆缓冲区-获取支撑数组的引用
            byte[] array = buffer.array();
            // 计算第一个字节的偏移量
            int offset = buffer.readerIndex() + buffer.arrayOffset();
            // 可读字节数
            int length = buffer.readableBytes();
            printBuffer(array, offset, length);
        }
        //
        byte b = buffer.readByte();//读一个字节
        // 检查是否是支撑数组
        if (buffer.hasArray()) {
            //堆缓冲区-获取支撑数组的引用
            byte[] array = buffer.array();
            // 计算第一个字节的偏移量
            int offset = buffer.readerIndex() + buffer.arrayOffset();
            // 可读字节数
            int length = buffer.readableBytes();
            printBuffer(array, offset, length);
        }
    }

    /**
     * 打印出Buffer的信息
     *
     * @param
     */
    private static void printBuffer(byte[] array, int offset, int len) {
        System.out.println("array：" + array);
        System.out.println("array->String：" + new String(array));
        System.out.println("offset：" + offset);
        System.out.println("len：" + len);
    }
}

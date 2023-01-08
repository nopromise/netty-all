package com.itcast.netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

@Slf4j
public class TestByteBuffer2 {
    /**
     * position是读/写位置。对于新缓冲区，它始终为 0。
     * limit有两个含义： 当我们写入缓冲区时，limit指示我们可以写入的位置。
     * 当我们从缓冲区读取时，limit指示缓冲区包含数据的位置。
     * 最初， ByteBuffer始终处于写入模式，并且limit等于capacity- 我们可以将空缓冲区填充到最后。
     * capacity指示缓冲区的大小。它在缓冲区的生命周期内不会改变。
     *
     * @param args
     */
    public static void main(String[] args) {
//        demo1();
        demo2();
    }

    public static void demo1() {
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        printMetrics(buffer);
    }

    /**
     * ByteBuffer 读写周期
     * 使用 put() 写入 ByteBuffer
     * 为了写入ByteBuffer，有多种put()方法可以将单个字节、字节数组或其他原始类型（如 char、double、float、int、long、short）写入缓冲区。
     */
    public static void demo2() {
        ByteBuffer buffer = ByteBuffer.allocate(1000);

        //1.使用put写入ByteBuffer
        for (int i = 0; i < 100; i++) {
            buffer.put((byte) 1);
        }
        //首先，我们将值 1 的 100 倍写入缓冲区，然后我们再次查看缓冲区指标：
        log.info("写100字节到buffer");
        printMetrics(buffer);
        //接下来，我们在缓冲区中写入 200 次 2。这次我们使用不同的方法：我们首先填充一个字节数组并将其复制到缓冲区中。最后，我们再次打印指标：
        byte[] twos = new byte[200];
        Arrays.fill(twos, (byte) 2);
        buffer.put(twos);
        log.info("写200字节到buffer");
        printMetrics(buffer);


        //2.使用 Buffer.flip() 切换到读取模式
        //对于从缓冲区读取，有相应的get()方法。例如，当使用Channel.write(buffer).
        //由于position不仅指示写入位置，还指示读取位置，因此我们必须position重新设置为 0。
        //同时，我们设置limit为 300 表示最多可以从缓冲区中读取 300 个字节。
        //在程序代码中，我们这样做：
      /*  buffer.limit(buffer.position());
        buffer.position(0);*/
        //由于每次从写入模式切换到读取模式时都需要这两行，因此有一种方法可以做到：
        buffer.flip();
        //position = 0, limit = 300, capacity = 1000
        log.info("调用flap，切换到读模式");
        printMetrics(buffer);

        //3.使用 get() 从 ByteBuffer 读取
        //假设我们要写入的通道当前只能占用 300 个字节中的 200 个。
        // 我们可以通过为该ByteBuffer.get()方法提供一个 200 字节大小的字节数组来模拟这一点，缓冲区应在其中写入其数据：
        buffer.get(new byte[200]);
        //position = 200, limit = 300, capacity = 1000
        //读取位置已经向右移动了 200 个字节——即到了已经读取数据的末尾，也就是我们还需要读取的数据的开始位置：
        log.info("读200字节后");
        printMetrics(buffer);

        //4.切换到写入模式 - 如何不这样做
        //细看该文章https://cloud.tencent.com/developer/article/1853890
        //现在要写回缓冲区，您可能会犯以下错误：您设置position了数据的末尾，即 300，然后limit又设置为 1000，这使我们回到了写完 1 和 2 之后的状态：
        //假设我们现在要向缓冲区写入 300 个字节。缓冲区将如下所示：
        //如果我们现在使用flip()切换回读取模式，position将回到 0：
        //但是，现在我们将再次读取我们已经读取的前 200 个字节。
        //因此，这种方法是错误的。以下部分说明如何正确执行此操作。
        //TODO 错误示范
/*
        buffer.position(300);
        buffer.limit(1000);
        printMetrics(buffer);
        byte[] threes = new byte[200];
        Arrays.fill(threes, (byte) 3);
        buffer.put(threes);
        log.info("写入300字节数字3");
        printMetrics(buffer);
        //
        log.info("flap方法切换到读模式");
        buffer.flip();
        printMetrics(buffer);
        //但是，现在我们将再次读取我们已经读取的前 200 个字节。
        //因此，这种方法是错误的。以下部分说明如何正确执行此操作。
 */
        //5.使用 Buffer.compact() 切换到写入模式
        //相反，当切换到写入模式时，我们必须按以下步骤进行：
        //我们计算剩余字节数：remaining = limit - position在示例中，结果为 100。
        //我们将剩余的字节移到缓冲区的开头。
        //我们将写入位置设置为左移字节的末尾，在示例中为 100。
        //我们设置limit到缓冲区的末尾。
        //ByteBuffer 还为此提供了一个方便的方法：
        buffer.compact();
        log.info("调用compact，继续写");
        printMetrics(buffer);
        //现在我们可以将接下来的 300 个字节写入缓冲区：
        byte[] threes = new byte[300];
        Arrays.fill(threes, (byte) 3);
        buffer.put(threes);
        log.info("写入300字节，继续写");
        printMetrics(buffer);
        //现在我们可以使用以下命令轻松切换回阅读模式flip()：
        buffer.flip();
        log.info("调flap，切换到读模式");
        printMetrics(buffer);
    }


    public static void printMetrics(ByteBuffer buffer) {
        System.out.printf("position = %4d, limit = %4d, capacity = %4d%n",
                buffer.position(), buffer.limit(), buffer.capacity());
    }
}

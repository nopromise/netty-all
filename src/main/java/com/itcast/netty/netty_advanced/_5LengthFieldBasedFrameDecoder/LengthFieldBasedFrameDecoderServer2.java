package com.itcast.netty.netty_advanced._5LengthFieldBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LengthFieldBasedFrameDecoderServer2 {
    /**
     * 使用EmbeddedChannel测试LengthFieldBasedFrameDecoder的编解码
     */
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(
                        1024,//最大长度
                        0,//表示长度的字段的偏移量是0
                        4,//表示长度字段的数据的长度
                        0,//
                        4),//剥离掉几个字节，然后再解析（把长度的字节剥离掉）
                new LoggingHandler(LogLevel.DEBUG)
        );

        //内容长度字段用4个字节表示
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        String content = "Hello, workld";
        buildMsg(buffer, content);
        buildMsg(buffer, "hi");
        buildMsg(buffer, "i want go usa");
        buildMsg(buffer, "i want go usa,and you want go where");

        //模拟入栈`
        //Write messages to the inbound of this Channel.
        channel.writeInbound(buffer);
    }

    private static void buildMsg(ByteBuf buffer, String content) {
        byte[] bytes = content.getBytes();//
        int length = bytes.length;
        buffer.writeInt(length);
        buffer.writeBytes(bytes);
    }
}
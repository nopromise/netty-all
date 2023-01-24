package com.itcast.netty.netty_advanced._8codec;

import cn.itcast.message.LoginRequestMessage;
import cn.itcast.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 编码器与解码器
 * <p>
 * 编解码器
 *
 * <p>
 * 自定义协议
 * 组成要素
 * 魔数：用来在第一时间判定接收的数据是否为无效数据包
 * 版本号：可以支持协议的升级
 * 序列化算法：消息正文到底采用哪种序列化反序列化方式
 * 如：json、protobuf、hessian、jdk
 * 指令类型：是登录、注册、单聊、群聊… 跟业务相关
 * 请求序号：为了双工通信，提供异步能力
 * 正文长度
 * 消息正文
 */
@Slf4j
public class MessageCodecTest extends ByteToMessageCodec<Message> {
    /**
     * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
     *
     * @param args
     * @throws Exception
     */
    //ByteToMessageCodec--字节到消息编解码器
    public static void main(String[] args) throws Exception {
        //测试
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG)
                //从第12个字节开始的4个字节表示长度
                //这样确保不会拆包和粘包
                , new LengthFieldBasedFrameDecoder(
                1024,
                12,
                4,
                0,
                0)
                , new MessageCodecTest()
        );
        //测试encode
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
//        channel.writeOutbound(message);
        //测试decode
        //先对数据进行编码
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodecTest().encode(null, message, buf);
        //
        channel.writeInbound(buf);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        //TODO 编码，把数据写入到out的ByteBuf

        //1. 4字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        //2. 1字节的版本
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(0);
        // 4. 1 字节的指令类型
        out.writeByte(msg.getMessageType());
        //5. 4个字节
        out.writeInt(msg.getSequenceId());
        // 无意义，对齐填充
        out.writeByte(0xff);
        //6. 获取内容的字节数组 序列化  jdk序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);

        //内容的字节数组
        byte[] bytes = bos.toByteArray();
        //7. 长度
        int length = bytes.length;
        out.writeInt(length);
        //8.写入内容
        out.writeBytes(bytes);
    }

    /**
     * 粘包和拆包？
     * 粘包：有长度字段，多余的不读取
     * 拆包：数据不够，比如读取不到长度字段等。如何处理？
     * 结合LengthFieldBasedFrameDecoder来处理。
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //TODO 解码，把in的byteBuf，解析成对象，存入到out的list

        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        //无意义的填充数据
        in.readByte();
        //数据对象内容的长度
        int length = in.readInt();
        //字节数组
        byte[] bytes = new byte[length];
        //从buf中读取指定长度的数据到bytes
        in.readBytes(bytes, 0, length);
        //jdk 反序列化
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        //添加到out中
        out.add(message);
    }
}

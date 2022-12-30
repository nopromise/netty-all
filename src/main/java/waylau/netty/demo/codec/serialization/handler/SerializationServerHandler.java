package waylau.netty.demo.codec.serialization.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import waylau.netty.demo.codec.serialization.bean.SerializationBean;

/**
 * SerializationServer Handler.
 * 
 * @since 1.0.0 2020年1月2日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
public class SerializationServerHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
		if (obj instanceof SerializationBean) {
			SerializationBean user = (SerializationBean) obj;
			ctx.writeAndFlush(user);
			System.out.println("Client -> Server: " + user);
		}
	}

}

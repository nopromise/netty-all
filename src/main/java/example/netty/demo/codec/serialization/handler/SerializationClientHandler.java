/**
 * 
 */
package example.netty.demo.codec.serialization.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import example.netty.demo.codec.serialization.bean.SerializationBean;

/**
 * SerializationClient Handler.
 * 
 * @since 1.0.0 2020年1月2日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
public class SerializationClientHandler extends
		SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		if (obj instanceof SerializationBean) {
			SerializationBean user = (SerializationBean) obj;
			System.out.println("Server -> Client: " + user);
		}

	}

}

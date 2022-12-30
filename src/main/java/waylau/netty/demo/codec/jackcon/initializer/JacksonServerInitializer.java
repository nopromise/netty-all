package waylau.netty.demo.codec.jackcon.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import waylau.netty.demo.codec.jackcon.codec.JacksonDecoder;
import waylau.netty.demo.codec.jackcon.codec.JacksonEncoder;
import waylau.netty.demo.codec.jackcon.entity.JacksonBean;
import waylau.netty.demo.codec.jackcon.handler.JacksonServerHandler;

/**
 * JacksonServer ChannelInitializer.
 * 
 * @since 1.0.0 2020年1月2日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
public class JacksonServerInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new JacksonDecoder<JacksonBean>(JacksonBean.class));
		pipeline.addLast(new JacksonEncoder());
		pipeline.addLast(new JacksonServerHandler());
	}
}
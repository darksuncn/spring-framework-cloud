package com.darksun.springCloud;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleTcpServerInitializer  extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

   pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
   pipeline.addLast("decoder", new StringDecoder());
   pipeline.addLast("encoder", new StringEncoder());
   pipeline.addLast("handler", new SimpleTcpServerHandler());

   System.out.println("SimpleChatClient:"+ch.remoteAddress() +" Connecting...");		
	}

}

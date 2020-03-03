package com.darksun.springCloud;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SimpleTcpServerHandler extends SimpleChannelInboundHandler<String> {

	 public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	 
	 private static SnowFlakeShortUrl snowFlake = new SnowFlakeShortUrl(2, 3);

   @Override
   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
       Channel incoming = ctx.channel();
    	 System.out.println("[SERVER] - " + incoming.remoteAddress() + " Enter\n");
       for (Channel channel : channels) {
           channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " Enter\n");
       }
       channels.add(ctx.channel());
   }

   @Override
   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
       Channel incoming = ctx.channel();
    	 System.out.println("[SERVER] - " + incoming.remoteAddress() + " Away\n");
       for (Channel channel : channels) {
           channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " Away\n");
       }
       channels.remove(ctx.channel());
   }
   @Override
   protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception { // (4)
       Channel incoming = ctx.channel();
       for (Channel channel : channels) {
           if (channel != incoming){
               channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
           } else {
               channel.writeAndFlush("[you-"+snowFlake.nextId()+"]" + s + "\n");
           }
       }
   }

   @Override
   public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
       Channel incoming = ctx.channel();
       System.out.println("SimpleChatClient:"+incoming.remoteAddress()+" ONLINE");
   }

   @Override
   public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
       Channel incoming = ctx.channel();
       System.out.println("SimpleChatClient:"+incoming.remoteAddress()+" OFFLINE");
   }
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
       Channel incoming = ctx.channel();
       System.out.println("SimpleChatClient:"+incoming.remoteAddress()+" EXCEPTION");
       // 当出现异常就关闭连接
       cause.printStackTrace();
       ctx.close();
   }
}

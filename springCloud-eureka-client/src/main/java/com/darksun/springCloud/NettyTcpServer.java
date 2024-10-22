package com.darksun.springCloud;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTcpServer {
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
    try {
      ServerBootstrap b = new ServerBootstrap(); // (2)
      b.group(bossGroup, workerGroup)
       .channel(NioServerSocketChannel.class) // (3)
       .childHandler(new SimpleTcpServerInitializer())  //(4)
       .option(ChannelOption.SO_BACKLOG, 128)          // (5)
       .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

      System.out.println("NettyTcpServer 启动了");

      // 绑定端口，开始接收进来的连接
      ChannelFuture f = b.bind(18081).sync(); // (7)

      // 等待服务器  socket 关闭 。
      // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
      f.channel().closeFuture().sync();

  } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();

      System.out.println("NettyTcpServer 关闭了");
  }		
		
		
	}

}

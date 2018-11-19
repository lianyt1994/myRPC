package com.zju.server;

import com.zju.test.ClientBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {

    // 如何符合约定，则调用本地方法，返回数据
    if (msg.toString().startsWith(ClientBootstrap.providerName)) {
      HelloService helloService = new HelloServiceImpl();
      String result = helloService.hello(msg.toString());
      //监听到就再写过去，实现循环
		Channel channel = ctx.channel();
		System.out.println("服务端打印channel"+channel);
		channel.writeAndFlush(result);
      System.out.println(helloService);
    }
  }
}

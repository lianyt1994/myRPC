package com.zju.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class HelloClientHandler extends ChannelInboundHandlerAdapter implements Callable {

  private ChannelHandlerContext context;
  private String result;
  private String para;

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    context = ctx;
  }

  /**
   * 收到服务端数据，唤醒等待线程
   */
  @Override
  public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
    result = msg.toString();
    notify();
  }

  /**
   * 写出数据，开始等待唤醒
   */
  @Override
  public synchronized Object call() throws InterruptedException {
    //这边再把之前放在para里面的值传过去
    //监听到就再写过去，实现循环
    Channel channel = context.channel();
    System.out.println("客户端打印channel"+channel);
    channel.writeAndFlush(para);
    wait();
    return result;
  }

  void setPara(String para) {
    this.para = para;
  }
}

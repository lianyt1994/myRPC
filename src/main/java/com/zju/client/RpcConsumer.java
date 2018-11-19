package com.zju.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

  private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private static HelloClientHandler client;

  /**
   * 创建一个代理对象
   */
  public Object createProxy(final Class<?> serviceClass,final String providerName) {
    return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[]{serviceClass},
            (proxy, method, args) -> {
          if (client == null) {
            initClient();
          }
          // 先把要传的值设置成para里面
          client.setPara(providerName + args[0]);
          return executor.submit(client).get();
        });
  }

  /**
   * 初始化客户端
   */
  private static void initClient() {
    client = new HelloClientHandler();
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();
    b.group(group)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new StringDecoder());
            p.addLast(new StringEncoder());
            p.addLast(client);
          }
        });
    try {
      //这里是connect和server的不一样
		ChannelFuture channelFuture = b.connect("localhost", 8088).sync();
		//future有个channel方法可以获得channel，进而继续读写
        System.out.println(channelFuture.channel());
	} catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

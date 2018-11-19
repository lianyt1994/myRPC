package com.zju.test;

import com.zju.client.RpcConsumer;
import com.zju.server.HelloService;

public class ClientBootstrap {

  public static final String providerName = "HelloService#hello#";

  public static void main(String[] args) throws InterruptedException {

    RpcConsumer consumer = new RpcConsumer();
    // 创建一个代理对象
    HelloService service = (HelloService) consumer.createProxy(HelloService.class, providerName);
    for (int i =0;i<10000 ;i++ ) {
      Thread.sleep(1000);
      System.out.println(service.hello("are you ok ?"+i));
    }
  }
}

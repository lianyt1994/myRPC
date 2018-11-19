package com.zju.server;

public class ServerBootstrap1 {
  public static void main(String[] args) {
    NettyServer.startServer("localhost", 8088);
  }
}
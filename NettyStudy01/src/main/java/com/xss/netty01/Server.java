package com.xss.netty01;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @ClassName Server
 * @Description Netty服务端
 * @Author xushaoshuai
 * @Parameters
 * @Date 2020/10/21 4:40 下午
 * @Return
 */
public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(8877));
        ss.accept();
        System.out.println("a client connect!");

    }
}

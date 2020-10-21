package com.xss.netty01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName Client
 * @Description Netty客户端
 * @Author xushaoshuai
 * @Parameters
 * @Date 2020/10/21 4:42 下午
 * @Return
 */
public class Client {
    public static void main(String[] args) throws Exception{
        //线程池
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();

        try {
            ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost",8877);

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(!future.isSuccess()){
                        System.out.println("not connect!");
                    }else {
                        System.out.println("connect");
                    }
                }
            });
            f.sync();
            System.out.println(".......");
        } finally {
            group.shutdownGracefully();
        }
    }
}
class ClientChannelInitializer extends ChannelInitializer{

    @Override
    protected void initChannel(Channel ch) throws Exception {
        System.out.println(ch);
    }
}

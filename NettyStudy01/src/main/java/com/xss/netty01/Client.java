package com.xss.netty01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.lang.ref.Reference;

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
                    .connect("localhost",8866);

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

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
class ClientChannelInitializer extends ChannelInitializer{

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ClientHandler());
    }
}
class ClientHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //channle 第一次连上可用，写出一个字符串 Direct Memory
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello".getBytes());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = null;
        try {
             buf = (ByteBuf)msg;
             byte[] bytes = new byte[buf.readableBytes()];
             buf.getBytes(buf.readerIndex(),bytes);
             System.out.println(new String(bytes));
        } finally {
            if(buf != null){
                ReferenceCountUtil.release(buf);
            }
        }

    }
}
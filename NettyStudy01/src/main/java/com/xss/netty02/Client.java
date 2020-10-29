package com.xss.netty02;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName Client
 * @Description Netty客户端
 * @Author xushaoshuai
 * @Parameters
 * @Date 2020/10/21 4:42 下午
 * @Return
 */
public class Client {
    private Channel channel = null;

    public void connect() {

        //线程池
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();

        try {
            ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost", 8866);

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        System.out.println("not connect!");
                    } else {
                        System.out.println("connect");
                        //initialize the channel
                        channel = future.channel();
                    }
                }
            });
            f.sync();
            System.out.println(".......");

            f.channel().closeFuture().sync();
            System.out.println("客户端已退出");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(String msg) {
        ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
        channel.writeAndFlush(buf);
    }

    //退出
    public void closeClient() {
        this.send("bye");
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.connect();
    }
}

class ClientChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ClientHandler());
    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {
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
            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String str = new String(bytes);
            ClientFrame.INSTANCE.updateText(str);
//             System.out.println(new String(bytes));
        } finally {
            if (buf != null) {
                ReferenceCountUtil.release(buf);
            }
        }

    }
}
package com.xss.netty02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @ClassName Server
 * @Description Netty服务端
 * @Author xushaoshuai
 * @Parameters
 * @Date 2020/10/21 4:40 下午
 * @Return
 */
public class Server {

    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static void main(String[] args) throws Exception {
//        ServerSocket ss = new ServerSocket();
//        ss.bind(new InetSocketAddress(8877));
//        ss.accept();
//        System.out.println("a client connect!");

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        try {
            ServerBootstrap b = new ServerBootstrap();
            ChannelFuture f = b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pl = ch.pipeline();
                            pl.addLast(new ServerChildHandler());
                        }
                    })
                    .bind(8866)
                    .sync();

            System.out.println("server started!");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

class ServerChildHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clients.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf)msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),bytes);
            String str = new String(bytes);
            if(str.equals("bye")){
                System.out.println("客户端要求退出");
                Server.clients.remove(ctx.channel());
                ctx.close();
            }else {
                Server.clients.writeAndFlush(msg);
            }
//            System.out.println(new String(bytes));
        }finally {
//            if(buf != null){
//                ReferenceCountUtil.release(buf);
//            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}
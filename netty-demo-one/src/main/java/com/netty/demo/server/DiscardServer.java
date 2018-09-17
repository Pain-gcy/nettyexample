package com.netty.demo.server;

import com.netty.demo.handler.ChildChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author guochunyuan
 * @create on  2018-09-17 11:36
 */
public class DiscardServer {
    public void run(int port) throws Exception {
        /**
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。
         * 在这个例子中我们实现了一个服务端的应用，
         * 因此会有2个NioEventLoopGroup会被使用。
         * 第一个经常被叫做‘boss’，用来接收进来的连接。
         * 第二个经常被叫做‘worker’，用来处理已经被接收的连接，
         * 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
         * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
         * 并且可以通过构造函数来配置他们的关系。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //进行网络通信（读写）
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("准备运行端口：" + port);
        try {
            //辅助工具类，用于服务器通道的一系列配置
            ServerBootstrap b = new ServerBootstrap();
//            绑定两个线程组
            b = b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //指定NIO的模式
            /**
             * 对于ChannelOption.SO_BACKLOG的解释：
             * 服务器端TCP内核维护有两个队列，我们称之为A、B队列。客户端向服务器端connect时，会发送带有SYN标志的包（第一次握手），服务器端
             * 接收到客户端发送的SYN时，向客户端发送SYN ACK确认（第二次握手），此时TCP内核模块把客户端连接加入到A队列中，然后服务器接收到
             * 客户端发送的ACK时（第三次握手），TCP内核模块把客户端连接从A队列移动到B队列，连接完成，应用程序的accept会返回。也就是说accept
             * 从B队列中取出完成了三次握手的连接。
             * A队列和B队列的长度之和就是backlog。当A、B队列的长度之和大于ChannelOption.SO_BACKLOG时，新的连接将会被TCP内核拒绝。
             * 所以，如果backlog过小，可能会出现accept速度跟不上，A、B队列满了，导致新的客户端无法连接。要注意的是，backlog对程序支持的
             * 连接数并无影响，backlog影响的只是还没有被accept取出的连接
             */
                    .option(ChannelOption.SO_BACKLOG, 128) //设置可接受的对列数
                    .childHandler(new ChildChannelHandler())//配置具体的数据处理方式
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024) //设置发送数据缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接受数据缓冲大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //保持连接;
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();
            //等待服务监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            //退出，释放线程资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new DiscardServer().run(8080);
    }
}

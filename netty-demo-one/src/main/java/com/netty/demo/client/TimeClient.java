package com.netty.demo.client;

import com.netty.demo.handler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author guochunyuan
 * @create on  2018-09-17 11:40
 */
public class TimeClient {
    public void connect(int port,String host)throws Exception{
        //配置客户端
        System.out.println(port+"--"+host);
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try {
            Bootstrap b=new Bootstrap();
            b.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 添加支持字符集
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            //添加处理器
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.connect(host,port).sync();
            //等待服务监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放线程资源
            eventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new TimeClient().connect(8080,"localhost");
    }
}

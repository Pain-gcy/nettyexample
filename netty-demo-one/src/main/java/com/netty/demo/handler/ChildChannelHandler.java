package com.netty.demo.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author guochunyuan
 * @create on  2018-09-17 11:37
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //数据处理
        socketChannel.pipeline().addLast(new DiscardServerHandler());
        //如果5s内没有与服务器童心的客户端自动断开
        socketChannel.pipeline().addLast(new ReadTimeoutHandler(5));
        // 添加支持字符集
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new StringEncoder());

    }
}

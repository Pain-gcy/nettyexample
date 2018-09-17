package com.netty.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @author guochunyuan
 * @create on  2018-09-17 11:40
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private byte[] req;
    public TimeClientHandler(){
        req="$tmb00035ET3318/08/22 11:5704026.75,027.31,20.00,20.00$".getBytes();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message;
        for(int i=0;i<10;i++){
            message= Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf in = (ByteBuf) msg;
            System.out.println(in.toString(CharsetUtil.UTF_8));
        }  finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }
}

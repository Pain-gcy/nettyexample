package com.netty.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @author guochunyuan
 * @create on  2018-09-17 11:37
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {
            String in = (String) msg;
            System.out.println("传输内容是");
//            String content = in.toString(CharsetUtil.UTF_8);
            System.out.println("收到信息:"+in);
            ByteBuf resp= Unpooled.copiedBuffer("我是服务返回的信息".getBytes());
            ctx.writeAndFlush(resp);
//          .addListener(ChannelFutureListener.CLOSE);
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

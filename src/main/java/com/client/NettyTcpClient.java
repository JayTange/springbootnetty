package com.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

@Component
public class NettyTcpClient {
    @Resource
    private Bootstrap bootstrap;

    @Value("${tcp.port}")
    private int tcpPort;

    @Value(("${tcp.ip}"))
    private String serverIp;

    private Channel channel;

    public void connect() throws Exception {
        // 发起异步连接
        ChannelFuture future = bootstrap.connect(serverIp, tcpPort).sync();
        channel = future.channel();
        if (!future.isSuccess()) {
            future.cause().printStackTrace();
        }

    }

    public void stop() throws Exception {
        channel.close();
    }

    public void sendMessage(String msg) {
        try {
            for (int i=0;i<10;i++){
                ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.getBytes("utf-8"));
                channel.writeAndFlush(byteBuf);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("数据发送失败");
        }

    }
}

package github.enplee.remoting.transport.client;

import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.transport.RpcRequestTransposter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 22:11
 *  @Description:
 */
public final class RpcNettyClient implements RpcRequestTransposter {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public RpcNettyClient() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap  = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                 .channel(NioSocketChannel.class)
                 .handler(new LoggingHandler(LogLevel.INFO))
                //TODO:
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ChannelPipeline p = ch.pipeline();
                         //TODO: 维持长连接
                         p.addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
/*                         p.addLast(new RpcMessageEncoder());
                         p.addLast(new RpcMessageDecoder());
                         p.addLast(new NettyRpcClietHanlder());*/
                     }
                 });
    }




    @Override
    public Object sendRpcRequest(RpcRequest request) {
        return null;
    }
}

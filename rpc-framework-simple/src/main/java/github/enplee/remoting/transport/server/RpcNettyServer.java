package github.enplee.remoting.transport.server;

import github.enplee.Utils.RuntimeUtil;
import github.enplee.remoting.transport.codec.RpcMessageDecoder;
import github.enplee.remoting.transport.codec.RpcMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 17:21
 *  @Description: server: start()
 */
@Slf4j
public class RpcNettyServer {

    public static final int PORT = 9090;


    @SneakyThrows
    public void start() {
        String host = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //TODO: threadFactory
        DefaultEventLoopGroup serviceHandlerGroup  = new DefaultEventLoopGroup(RuntimeUtil.cpus());

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childOption(ChannelOption.TCP_NODELAY,true)
                     .childOption(ChannelOption.SO_KEEPALIVE,true)
                     .handler(new LoggingHandler(LogLevel.INFO))
                     .childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                         @Override
                         protected void initChannel(NioServerSocketChannel ch) throws Exception {
                             ChannelPipeline p = ch.pipeline();
                             p.addLast(new RpcMessageEncoder());
                             p.addLast(new RpcMessageDecoder());
                             p.addLast(serviceHandlerGroup,new RpcNettyServerHanlder());
                         }
                     });
            // 绑定端口 同步等待成功
            ChannelFuture f = bootstrap.bind(host, PORT).sync();
            // 同步等待服务器端口关闭
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            log.error("occur exception when start server:",e);
        }finally {
            log.error("shutdown boosGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }
}

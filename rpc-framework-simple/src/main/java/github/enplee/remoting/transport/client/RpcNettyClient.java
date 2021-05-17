package github.enplee.remoting.transport.client;

import github.enplee.enums.CompressTypeEnum;
import github.enplee.enums.SerializationTypeEnum;
import github.enplee.factory.SingletonFactory;
import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.dto.RpcResponce;
import github.enplee.remoting.transport.RpcRequestTransposter;
import github.enplee.remoting.transport.codec.RpcMessageDecoder;
import github.enplee.remoting.transport.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 22:11
 *  @Description: request: 1. new boost/eventLoop 2.get channel 3. channel.writeAndFlush() 4.future get response.
 */

@Slf4j
public final class RpcNettyClient implements RpcRequestTransposter {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final ChannelProvider channelProvider;
    private final UnprocessedRequests unprocessedRequests;
    private final int port = 9090;
    private final RpcNettyClient rpcNettyClient = this;

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
                         p.addLast(new RpcMessageEncoder());
                         p.addLast(new RpcMessageDecoder());
                         p.addLast(new RpcNettyClientHanlder(rpcNettyClient));
                     }
                 });
        // 以下的容器应该是单例的
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * Channel 复用: 传统：channel = bootstrap.connect(InetAddress)
     */
    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    @Override
    public Object sendRpcRequest(RpcRequest request) {
        //send where
        //TODO: 服务发现机制

        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        //using channel get channel
        Channel channel = getChannel(inetSocketAddress);
        CompletableFuture<RpcResponce<Object>> responceFuture = new CompletableFuture<>();
        if(channel.isActive()){
            unprocessedRequests.put(request.getRequestId(),responceFuture);
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setBody(request);
            rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
            rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
            rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future ->{
               if(future.isSuccess()){
                   log.info("client send message: [{}]", rpcMessage);
               }else {
                   future.channel().close();
                   responceFuture.completeExceptionally(future.cause());
                   log.error("Send faild:", future.cause());
               }
            });
        }else {
            throw new IllegalStateException();
        }
        //return responce
        return responceFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress){
        Channel channel = channelProvider.get(inetSocketAddress);
        if(channel == null){
            try {
                channel = doConnect(inetSocketAddress);
                channelProvider.set(inetSocketAddress,channel);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return channel;
    }
    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}

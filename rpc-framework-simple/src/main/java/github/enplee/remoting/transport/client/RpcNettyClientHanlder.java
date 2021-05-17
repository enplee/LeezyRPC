package github.enplee.remoting.transport.client;

import github.enplee.enums.CompressTypeEnum;
import github.enplee.enums.SerializationTypeEnum;
import github.enplee.factory.SingletonFactory;
import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import github.enplee.remoting.dto.RpcResponce;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.protostuff.Rpc;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 15:04
 *  @Description:
 */
@Slf4j
public class RpcNettyClientHanlder extends ChannelInboundHandlerAdapter {
    // 对一个客户端来说，所有的unprocessRequest 容器应该是单例的
    private final UnprocessedRequests unprocessedRequests;
    private final RpcNettyClient rpcNettyClient;

    public RpcNettyClientHanlder(RpcNettyClient rpcNettyClient) {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.rpcNettyClient  = rpcNettyClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcMessage) {
            RpcMessage message = (RpcMessage) msg;
            byte messageType = message.getMessageType();
            if(messageType == RpcConstants.HEARTBEAT_RESPONCE_TYPE) {
                log.info("heart [{}]",message.getBody());
            }else if(messageType == RpcConstants.RESPONCE_TYPE){
                RpcResponce<Object> rpcResponce = (RpcResponce<Object>) message.getBody();
                unprocessedRequests.complete(rpcResponce);
            }
        }
    }

    /**
     * 长时间没有channelRead
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState idleState = ((IdleStateEvent)evt).state();
            if(idleState == IdleState.WRITER_IDLE){
                log.info("write idle happed [{}]",ctx.channel().remoteAddress());
                Channel channel = rpcNettyClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                rpcMessage.setBody(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }

    /**
     * Called when an exception occurs in processing a client message
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}

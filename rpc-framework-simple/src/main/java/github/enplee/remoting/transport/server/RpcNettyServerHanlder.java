package github.enplee.remoting.transport.server;


import github.enplee.enums.CompressTypeEnum;
import github.enplee.enums.RpcResponseCodeEnum;
import github.enplee.enums.SerializationTypeEnum;
import github.enplee.factory.SingletonFactory;
import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.dto.RpcResponce;
import github.enplee.remoting.handler.RpcRequestHanlder;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 19:41
 *  @Description: 处理client发送的request
 */
@Slf4j
public class RpcNettyServerHanlder extends ChannelInboundHandlerAdapter {

    private final RpcRequestHanlder rpcRequestHanlder;

    public RpcNettyServerHanlder() {
        this.rpcRequestHanlder = SingletonFactory.getInstance(RpcRequestHanlder.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof RpcMessage){
                log.info("server receiver msg: [{}]",msg);
                RpcMessage message = (RpcMessage)msg;
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
                //rpcMessage.setRequestId(message.getRequestId());
                byte messageType = message.getMessageType();
                if(messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE){
                    rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONCE_TYPE);
                    rpcMessage.setBody(RpcConstants.PONG);
                }else if(messageType == RpcConstants.REQUEST_TYPE){
                    // handle the request and call the method
                    RpcRequest rpcRequest = (RpcRequest)message.getBody();
                    // TODO：call method get result -- done
                    Object result = rpcRequestHanlder.handle(rpcRequest);
                    rpcMessage.setMessageType(RpcConstants.RESPONCE_TYPE);
                    if(ctx.channel().isActive() && ctx.channel().isWritable()){
                        RpcResponce<Object> responce = RpcResponce.success(result, rpcRequest.getRequestId());
                        rpcMessage.setBody(responce);
                    }else {
                        RpcResponce<Object> responce = RpcResponce.fail(RpcResponseCodeEnum.FAIL);
                        rpcMessage.setBody(responce);
                    }
                    // TODO: analize
                    log.info("send rpcMessage: [{}]",rpcMessage);
                    ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state =  ((IdleStateEvent)evt).state();
            if(state == IdleState.READER_IDLE){
                log.info("idle check happen,so clse the connection");
                ctx.close();
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}

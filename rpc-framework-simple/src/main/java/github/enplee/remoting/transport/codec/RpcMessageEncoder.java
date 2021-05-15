package github.enplee.remoting.transport.codec;

import github.enplee.enums.CompressTypeEnum;
import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  @author: leezy
 *  @Date: 2021/5/15 22:06
 *  @Description:
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            // leave place write full length
            out.writerIndex(out.writerIndex()+4);
            out.writeByte(msg.getMessageType());
            out.writeByte(msg.getCodec());
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeByte(ATOMIC_INTEGER.getAndIncrement());
            // build full length
            byte[] bodyBytes = null;
            out.writeInt(msg.getRequestId());

        }catch (Exception e){
            log.error("Encode request error",e);
        }
    }
}

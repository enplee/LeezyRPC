package github.enplee.remoting.transport.codec;

import github.enplee.compress.Compressor;
import github.enplee.compress.gzip.GzipCompressor;
import github.enplee.enums.CompressTypeEnum;
import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import github.enplee.serialize.Serializer;
import github.enplee.serialize.kyro.KyroSerializer;
import github.enplee.serialize.protostuff.ProtoStuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  @author: leezy
 *  @Date: 2021/5/15 22:06
 *  @Description:  MassageToByteEncoder<RpcMessage> --> RpcMessage to Bytes
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        try {
            log.info("decode message : [{}]",msg);
            // write message head
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            out.writerIndex(out.writerIndex()+4); // leave fullLength
            out.writeByte(msg.getMessageType());
            out.writeByte(msg.getCodec());
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            // write message body
            byte[] bodyBytes = null;
            int fullLength = RpcConstants.MAX_HEAD_LENGTH;
            if(msg.getMessageType() != RpcConstants.HEARTBEAT_REQUEST_TYPE &&
               msg.getMessageType() != RpcConstants.HEARTBEAT_RESPONCE_TYPE) {
                //TODO: SPI机制
                Serializer ser = new ProtoStuffSerializer();
                bodyBytes = ser.serialize(msg.getBody());
                Compressor compressor = new GzipCompressor();
                bodyBytes = compressor.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }
            if(bodyBytes != null) {
                log.info("ready to sent MessageBody,length [{}]",bodyBytes.length);
                out.writeBytes(bodyBytes);
            }
            // write fullLength and recover offset to the end
            int writeIdx = out.writerIndex();
            out.writerIndex(writeIdx - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIdx);
        }catch (Exception e){
            log.error("Encode request error",e);
        }
    }
}

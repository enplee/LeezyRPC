package github.enplee.remoting.transport.codec;

import github.enplee.compress.Compressor;
import github.enplee.compress.gzip.GzipCompressor;
import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.dto.RpcResponce;
import github.enplee.serialize.Serializer;
import github.enplee.serialize.protostuff.ProtoStuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


/**
 *  @author: leezy
 *  @Date: 2021/5/14 22:11
 *  @Description:
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder(){
        /**
         *  lengthFieldOffset = magic code (4B) + version(1B) = 5B
         *  lengthFieldLength = full lenth is 4B
         *  lengthAdjustment = start offset = -4B -5B
         *  initialBytesToStrip = we need magic code and version,so do not need trip. 0B
         */
        this(RpcConstants.MAX_FRAME_LENGTH,5,4,-9,0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        log.info("ByteBuf in size {}",in);
        Object decoded = super.decode(ctx, in);
        if(decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if(frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                }catch (Exception e){
                    log.error("Decode frame error!",e);
                }finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        checkMagicNumber(in);
        checkVersion(in); //  offset -> 5
        int fullLength = in.readInt();
        byte messageType = in.readByte();
        byte codec = in.readByte();
        byte compress = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder().messageType(messageType).codec(codec).compress(compress).requestId(requestId).build();

        if(messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE){
            rpcMessage.setBody(RpcConstants.PING);
            return rpcMessage;
        }
        if(messageType == RpcConstants.HEARTBEAT_RESPONCE_TYPE){
            rpcMessage.setBody(RpcConstants.PONG);
            return rpcMessage;
        }

        int bodyLength = fullLength-RpcConstants.MAX_HEAD_LENGTH;
        if(bodyLength > 0) {
            byte[] bytes = new byte[bodyLength];
            in.readBytes(bytes);
            //TODO: 使用SPI机制，让接口和实现解耦
            //decompress the bytes accoding to the compressType
            Compressor compressor = new GzipCompressor();
            byte[] decompress = compressor.decompress(bytes);
            //deserialize the bytes accoding to the codec
            Serializer serializer = new ProtoStuffSerializer();
            if(messageType == RpcConstants.REQUEST_TYPE){
                RpcRequest rpcRequest = serializer.deSerialize(decompress, RpcRequest.class);
                rpcMessage.setBody(rpcRequest);
            }
            if(messageType == RpcConstants.RESPONCE_TYPE){
                RpcResponce rpcResponce = serializer.deSerialize(decompress, RpcResponce.class);
                rpcMessage.setBody(rpcResponce);
            }
        }
        return rpcMessage;
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION){
            throw new RuntimeException("version isn`t compatible" + version);
        }
    }
    private void checkMagicNumber(ByteBuf in) {
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] read = new byte[len];
        in.readBytes(read);
        for(int i=0;i<len;i++){
            if(read[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknow magic code" + Arrays.toString(read));
            }
        }
    }
}

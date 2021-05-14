package github.enplee.remoting.transport.codec;

import github.enplee.remoting.consts.RpcConstants;
import github.enplee.remoting.dto.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.protostuff.Rpc;

import java.util.Arrays;


/**
 *  @author: leezy
 *  @Date: 2021/5/14 22:11
 *  @Description:
 */
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
        Object decoded = super.decode(ctx, in);
        if(decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if(frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {

            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        checkMagicNumber(in);
        checkVersion(in); //  offset -> 5
        int fullLength = in.readInt();
        //TODO: 构建PpcMessage & DeCompress/DeSerialize body
        return null;
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

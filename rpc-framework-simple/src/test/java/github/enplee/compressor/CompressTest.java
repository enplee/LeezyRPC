package github.enplee.compressor;

import github.enplee.compress.gzip.GzipCompressor;
import github.enplee.remoting.dto.RpcRequest;
import github.enplee.serialize.kyro.KyroSerializer;
import org.junit.Test;

public class CompressTest {

    @Test
    public void compressTest(){
        KyroSerializer ser = new KyroSerializer();
        RpcRequest request = RpcRequest.builder().serviceName("service").methordName("method").args(new Object[]{"args"}).argsType(new Class[]{String.class}).build();
        byte[] serialize = ser.serialize(request);

        final GzipCompressor gzip = new GzipCompressor();
        final byte[] compress = gzip.compress(serialize);
        final byte[] decompress = gzip.decompress(compress);
        System.out.println(compress.length);
        System.out.println(decompress.length);

        final RpcRequest rpcRequest = ser.deSerialize(decompress, RpcRequest.class);
        System.out.println(rpcRequest.toString());
    }
}

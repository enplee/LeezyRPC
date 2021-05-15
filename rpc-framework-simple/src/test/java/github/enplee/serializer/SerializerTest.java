package github.enplee.serializer;

import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.dto.RpcResponce;
import github.enplee.serialize.kyro.KyroSerializer;
import github.enplee.serialize.protostuff.ProtoStuffSerializer;
import io.protostuff.Rpc;
import org.apache.zookeeper.common.Time;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

public class SerializerTest {

    @Test
    public void testKyroSerializer(){
        KyroSerializer ser = new KyroSerializer();
        RpcRequest request = RpcRequest.builder().serviceName("service").methordName("method").args(new Object[]{"args"}).argsType(new Class[]{String.class}).build();
        byte[] serialize = ser.serialize(request);
        System.out.println(serialize.length);

        final long start = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            request = RpcRequest.builder().serviceName(String.valueOf(i)).methordName(String.valueOf(i)).args(new Object[]{"args"}).argsType(new Class[]{String.class}).build();
            final byte[] serialized = ser.serialize(request);
        }
        final long end = System.currentTimeMillis();
        System.out.println("process time： "+ (end-start) + "ms");
    }

    @Test
    public void testProtoStuffSerializer(){
        ProtoStuffSerializer ser = new ProtoStuffSerializer();
        RpcRequest request = RpcRequest.builder().serviceName("service").methordName("method").args(new Object[]{"args"}).argsType(new Class[]{String.class}).build();
        byte[] serialize = ser.serialize(request);
        System.out.println(serialize.length);

        final long start = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            request = RpcRequest.builder().serviceName(String.valueOf(i)).methordName(String.valueOf(i)).args(new Object[]{"args"}).argsType(new Class[]{String.class}).build();
            final byte[] serialized = ser.serialize(request);
        }
        final long end = System.currentTimeMillis();
        System.out.println("process time： "+ (end-start) + "ms");
    }
}

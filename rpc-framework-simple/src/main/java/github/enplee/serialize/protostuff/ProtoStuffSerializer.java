package github.enplee.serialize.protostuff;

import github.enplee.serialize.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 *  @author: leezy
 *  @Date: 2021/5/15 9:56
 *  @Description: protoStuff serializer https://protostuff.github.io/docs/
 */
public class ProtoStuffSerializer implements Serializer {

    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] Serialize(Object object) {
        Class<?> clazz = object.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] bytes;
        try {
            bytes = ProtobufIOUtil.toByteArray(object, schema, BUFFER);
        }finally {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T DeSerialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes,obj,schema);
        return obj;
    }
}

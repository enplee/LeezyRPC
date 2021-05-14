package github.enplee.serialize.protostuff;

import github.enplee.serialize.Serializer;

public class ProtoStuffSerializer implements Serializer {
    @Override
    public byte[] Serialize(Object object) {
        return new byte[0];
    }

    @Override
    public <T> T DeSerialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}

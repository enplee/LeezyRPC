package github.enplee.serialize.kyro;

import github.enplee.serialize.Serializer;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 22:11
 *  @Description:
 */ 
public class KyroSerializer implements Serializer {
    @Override
    public byte[] Serialize(Object object) {
        return new byte[0];
    }

    @Override
    public <T> T DeSerialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}

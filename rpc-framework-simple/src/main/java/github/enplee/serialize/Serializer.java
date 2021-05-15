package github.enplee.serialize;

import java.io.Serializable;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 22:07
 *  @Description: 序列化器接口，定义两个method: Serialize & DeSerialize
 */
public interface Serializer {

    byte[] serialize(Object object);

    <T> T seSerialize(byte[] bytes,Class<T> clazz);
}

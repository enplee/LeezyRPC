package github.enplee.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 15:14
 *  @Description: 维护单例对象的工厂
 */
public final class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        String key = clazz.toString();
        if(OBJECT_MAP.containsKey(key)){
            return clazz.cast(OBJECT_MAP.get(key));
        }else {
            return clazz.cast(OBJECT_MAP.computeIfAbsent(key, k ->{
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }));
        }
    }
}

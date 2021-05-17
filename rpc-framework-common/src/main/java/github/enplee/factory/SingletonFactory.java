package github.enplee.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 15:14
 *  @Description: 维护单例对象的工厂
 */
public class SingletonFactory {
    private static final Map<String,Object> OBJECT_MAP = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> clazz) {
        String key = clazz.toString();
        Object instance;
        synchronized (SingletonFactory.class){
            instance = OBJECT_MAP.get(key);
            if(instance == null){
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                    OBJECT_MAP.put(key,instance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return clazz.cast(instance);
    }
}

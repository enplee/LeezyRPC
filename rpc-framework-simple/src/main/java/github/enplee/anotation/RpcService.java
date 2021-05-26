package github.enplee.anotation;

import java.lang.annotation.*;

/**
 *  @author: leezy
 *  @Date: 2021/5/25 17:22
 *  @Description: 用来标记service的实现类，用于Spring容器的自动管理
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    String version() default "";

    String group() default  "";
}

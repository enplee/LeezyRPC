package github.enplee.anotation;

import java.lang.annotation.*;

/**
 *  @author: leezy
 *  @Date: 2021/5/25 17:25
 *  @Description: 标记对service的消费,通过Spring进行自动注入
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcRefence {

    String version() default "";

    String group() default "";
}

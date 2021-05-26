package github.enplee.spring;

import github.enplee.config.RpcServiceConfig;
import github.enplee.factory.SingletonFactory;
import github.enplee.provider.ServiceProvider;
import github.enplee.provider.impl.ServiceProviderImpl;
import github.enplee.proxy.RpcClientProxy;
import github.enplee.remoting.transport.RpcRequestTransposter;
import github.enplee.remoting.transport.client.RpcNettyClient;
import github.enplee.anotation.RpcRefence;
import github.enplee.anotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 *  @author: leezy
 *  @Date: 2021/5/25 17:28
 *  @Description: 在bean加载前后进行处理，处理RpcService和RpcRefence注解进行服务的注册和消费
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private  final ServiceProvider serviceProvider;
    private  final RpcRequestTransposter rpcRequestTransposter;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
        this.rpcRequestTransposter = SingletonFactory.getInstance(RpcNettyClient.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 判断是不是含有Rpcservce注解->解析注解组装config-> serviceProvider::publishService
        if(bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with [{}]", bean.getClass().getName(),RpcService.class.getCanonicalName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            log.info("beanName test : [{}]",beanName);
            RpcServiceConfig serviceConfig = RpcServiceConfig.builder()
                                            .service(bean)
                                            .group(rpcService.group())
                                            .version(rpcService.version()).build();
            serviceProvider.publishService(serviceConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 检查filed是否由RpcReference注解
        log.info("执行postProcessAfterInitialization");
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for(Field declaredField : declaredFields) {
            RpcRefence annotation = declaredField.getAnnotation(RpcRefence.class);
            if(annotation != null ) {

                RpcServiceConfig serviceConfig = RpcServiceConfig.builder()
                                                .group(annotation.group())
                                                .version(annotation.version()).build();
                log.info("serviceConfig: [{}]",serviceConfig);
                RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceConfig, rpcRequestTransposter);
                Object proxy = rpcClientProxy.getProxy(declaredField.getType());
                try {
                    declaredField.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}

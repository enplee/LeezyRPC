package github.enplee.remoting.handler;

import github.enplee.exception.RpcException;
import github.enplee.factory.SingletonFactory;
import github.enplee.provider.ServiceProvider;
import github.enplee.provider.impl.ServiceProviderImpl;
import github.enplee.registry.ServiceRegistry;
import github.enplee.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  @author: leezy
 *  @Date: 2021/5/23 16:21
 *  @Description: 处理Rpcrequest的方法调用
 *  1. 从serviceProvider容器中取得服务对象
 *  2. 利用反射的方式，调用service对象对应的方法
 *  3. 返回方法调用的结果
 */

@Slf4j
public class RpcRequestHanlder {
    private final ServiceProvider serviceProvider;

    public RpcRequestHanlder() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getServiceName());
        return invokeTargetMethod(rpcRequest,service);
    }

    public Object invokeTargetMethod(RpcRequest request, Object service) {
        Object result = null;
        // reflect
        try {
            Method method = service.getClass().getMethod(request.getMethordName(), request.getArgsType());
            result = method.invoke(service,request.getArgs());
            log.info("service: [{}] successful invoke method [{}]", request.getServiceName(), request.getMethordName());
        } catch (NoSuchMethodException  | IllegalAccessException | InvocationTargetException e) {
            throw new RpcException(e.getMessage(),  e);
        }
        return result;
    }
}

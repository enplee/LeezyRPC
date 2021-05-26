package github.enplee.proxy;


import github.enplee.config.RpcServiceConfig;
import github.enplee.enums.RpcErrorMessageEnum;
import github.enplee.enums.RpcResponseCodeEnum;
import github.enplee.exception.RpcException;
import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.dto.RpcResponce;
import github.enplee.remoting.transport.RpcRequestTransposter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 *  @author: leezy
 *  @Date: 2021/5/23 17:20
 *  @Description:
 *  1.创建Service的代理对象，用户调用service::method时，相当于调用invoke方法。
 *  2. invoke方法中，封装了RPC请求的一些列动作：打包RPCRequest，NettyClient::SentMessage,futrue::get。
 */

@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName :";
    private final RpcServiceConfig rpcServiceConfig;
    private final RpcRequestTransposter rpcRequestTransposter;

    public RpcClientProxy(RpcServiceConfig rpcServiceConfig, RpcRequestTransposter rpcRequestTransposter) {
        this.rpcServiceConfig = rpcServiceConfig;
        this.rpcRequestTransposter = rpcRequestTransposter;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},  this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //封装RpcReques
        log.info("invoke method [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methordName(method.getName())
                                .args(args)
                                .argsType(method.getParameterTypes())
                                .requestId(UUID.randomUUID().toString())
                                .group(rpcServiceConfig.getGroup())
                                .version(rpcServiceConfig.getVersion())
                                .serviceName(method.getDeclaringClass().getName()).build();
        // rpcTransport::sendRpcRequest
        RpcResponce<Object> rpcResponce = null;
        CompletableFuture<RpcResponce<Object>> future = (CompletableFuture<RpcResponce<Object>>) rpcRequestTransposter.sendRpcRequest(rpcRequest);
        log.info("get future,wait process coming......");
        // future::get
        rpcResponce = future.get();
        // check
        this.check(rpcRequest,rpcResponce);
        //return
        return rpcResponce.getData();
    }

    public void check(RpcRequest rpcRequest, RpcResponce<Object> rpcResponce) {
        // judge null：invoke fail
        if(rpcResponce == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOKATION_FAILURE,INTERFACE_NAME+rpcRequest.getServiceName());
        }
        // requestId：request resp not match
        if(!rpcRequest.getRequestId().equals(rpcResponce.getRequstId())){
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_REQUEST,INTERFACE_NAME+rpcRequest.getServiceName());
        }
        // getCode： service invoke fail
        if(rpcResponce.getCode() == null || !rpcResponce.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOKATION_FAILURE,INTERFACE_NAME+rpcRequest.getServiceName());
        }
    }
}

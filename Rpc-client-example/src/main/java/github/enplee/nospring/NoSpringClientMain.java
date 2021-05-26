package github.enplee.nospring;

import github.enplee.Test;
import github.enplee.TestService;
import github.enplee.config.RpcServiceConfig;
import github.enplee.proxy.RpcClientProxy;
import github.enplee.remoting.transport.client.RpcNettyClient;

public class NoSpringClientMain {
    public static void main(String[] args) {
        RpcNettyClient rpcNettyClient = new RpcNettyClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcServiceConfig, rpcNettyClient);
        TestService testService = rpcClientProxy.getProxy(TestService.class);
        String test = testService.test(new Test("test", " using no Spring"));
        System.out.println(test);
        rpcNettyClient.close();
    }
}

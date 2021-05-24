package github.enplee.nospring;

import github.enplee.Test;
import github.enplee.TestService;
import github.enplee.config.RpcSerciceConfig;
import github.enplee.proxy.RpcClientProxy;
import github.enplee.remoting.transport.client.RpcNettyClient;

public class NoSpringClientMain {
    public static void main(String[] args) {
        RpcNettyClient rpcNettyClient = new RpcNettyClient();
        RpcSerciceConfig rpcSerciceConfig = new RpcSerciceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcSerciceConfig, rpcNettyClient);
        TestService testService = rpcClientProxy.getProxy(TestService.class);
        String test = testService.test(new Test("test", " using no Spring"));
        System.out.println(test);
        rpcNettyClient.close();
    }
}

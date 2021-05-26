package github.enplee.nospring;

import github.enplee.config.RpcServiceConfig;
import github.enplee.remoting.transport.server.RpcNettyServer;
import github.enplee.serviceImpl.TestServiceImpl;

public class NoSpringServerMain {
    public static void main(String[] args) {
        TestServiceImpl testService = new TestServiceImpl();
        RpcNettyServer rpcNettyServer = new RpcNettyServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(testService);
        rpcNettyServer.registerService(rpcServiceConfig);
        rpcNettyServer.start();
    }
}

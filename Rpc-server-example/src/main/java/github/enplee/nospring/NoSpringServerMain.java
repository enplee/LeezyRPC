package github.enplee.nospring;

import github.enplee.config.RpcSerciceConfig;
import github.enplee.remoting.transport.server.RpcNettyServer;
import github.enplee.serviceImpl.TestServiceImpl;

public class NoSpringServerMain {
    public static void main(String[] args) {
        TestServiceImpl testService = new TestServiceImpl();
        RpcNettyServer rpcNettyServer = new RpcNettyServer();
        RpcSerciceConfig rpcSerciceConfig = new RpcSerciceConfig();
        rpcSerciceConfig.setService(testService);
        rpcNettyServer.registerService(rpcSerciceConfig);
        rpcNettyServer.start();
    }
}

package github.enplee.provider.impl;

import github.enplee.config.RpcServiceConfig;
import github.enplee.enums.RpcErrorMessageEnum;
import github.enplee.exception.RpcException;
import github.enplee.factory.SingletonFactory;
import github.enplee.provider.ServiceProvider;
import github.enplee.registry.ServiceRegistry;
import github.enplee.registry.zk.ZkserviceRegistryImpl;
import github.enplee.remoting.transport.server.RpcNettyServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private final Map<String, Object> serviceMap;
    private final Set<String> registedServiceSet;
    private final ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        this.serviceMap = new ConcurrentHashMap<>();
        this.registedServiceSet = ConcurrentHashMap.newKeySet();
        //TODO: spi
        this.serviceRegistry = SingletonFactory.getInstance(ZkserviceRegistryImpl.class);
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if(registedServiceSet.contains(rpcServiceName)){
            return;
        }
        registedServiceSet.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service [{}] and interface:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if(service == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND,rpcServiceName);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String rpcServiceName = rpcServiceConfig.getRpcServiceName();
            addService(rpcServiceConfig);
            String host = InetAddress.getLocalHost().getHostAddress();
            serviceRegistry.registerService(rpcServiceName,new InetSocketAddress(host, RpcNettyServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAdress: ",e);
        }
    }
}

package github.enplee.provider;

import github.enplee.config.RpcServiceConfig;

/**
 *  @author: leezy
 *  @Date: 2021/5/20 15:17
 *  @Description: Interface to ServviceProvider
 *
 */
public interface ServiceProvider {

    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    void publishService(RpcServiceConfig rpcServiceConfig);
}

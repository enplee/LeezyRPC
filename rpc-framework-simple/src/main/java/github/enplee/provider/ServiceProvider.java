package github.enplee.provider;

import github.enplee.config.RpcSerciceConfig;

/**
 *  @author: leezy
 *  @Date: 2021/5/20 15:17
 *  @Description: Interface to ServviceProvider
 *
 */
public interface ServiceProvider {

    void addService(RpcSerciceConfig rpcSerciceConfig);

    Object getService(String rpcServiceName);

    void publishService(RpcSerciceConfig rpcSerciceConfig);
}

package github.enplee.registry;

import github.enplee.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    InetSocketAddress lookupService(RpcRequest request);

}

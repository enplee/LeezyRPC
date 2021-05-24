package github.enplee.registry;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface ServiceRegistry {

    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}

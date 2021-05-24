package github.enplee.registry.zk;

import github.enplee.registry.ServiceRegistry;
import github.enplee.registry.zk.utils.CuratorUtil;
import org.apache.curator.framework.CuratorFramework;
import java.net.InetSocketAddress;

public class ZkserviceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String rpcServicePath = CuratorUtil.PRC_REGISTY_ROOT + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        CuratorUtil.createPersistentNode(zkClient,rpcServicePath);
    }
}

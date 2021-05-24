package github.enplee.registry.zk;

import github.enplee.enums.RpcErrorMessageEnum;
import github.enplee.exception.RpcException;
import github.enplee.loadbalance.LoadBalance;
import github.enplee.loadbalance.loadbalancer.RandomLoadbalance;
import github.enplee.registry.ServiceDiscovery;
import github.enplee.registry.zk.utils.CuratorUtil;
import github.enplee.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        //TODO: SPI
        this.loadBalance = new RandomLoadbalance();
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest request) {

        String serviceName = request.getServiceName();
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        List<String> serviceList = CuratorUtil.getChildrenNodes(zkClient, serviceName);
        // 直面用户的异常，自己构建
        if(serviceList == null || serviceList.size() == 0){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND,serviceName);
        }

        // load balancing
        String targetServiceAddress = loadBalance.selectServiceAddress(serviceList, request);
        log.info("Successfully found the service address: [{}]", targetServiceAddress);
        String[] socketArray = targetServiceAddress.split(":");
        return new InetSocketAddress(socketArray[0],Integer.parseInt(socketArray[1]));
    }
}

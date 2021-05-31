package github.enplee.loadbalance.loadbalancer;

import github.enplee.loadbalance.AbstractLoadBalance;
import github.enplee.remoting.dto.RpcRequest;

import java.util.List;

public class ConsistentHashLoadbalacne extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest request) {
        return null;
    }
}

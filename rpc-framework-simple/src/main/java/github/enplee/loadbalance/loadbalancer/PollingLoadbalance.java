package github.enplee.loadbalance.loadbalancer;

import github.enplee.loadbalance.AbstractLoadBalance;
import github.enplee.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PollingLoadbalance extends AbstractLoadBalance {
    private final Map<String,Integer> indexMap = new ConcurrentHashMap<>();
    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest request) {
        Integer index = indexMap.get(serviceAddresses.get(0));
        if(index == null) {
            index = 0;
        }else {
            index = (index+1)%(serviceAddresses.size());
        }
        indexMap.put(serviceAddresses.get(0),index);
        return serviceAddresses.get(index);
    }
}

package github.enplee.loadbalance.loadbalancer;

import github.enplee.loadbalance.AbstractLoadBalance;
import github.enplee.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 *  @author: leezy
 *  @Date: 2021/5/20 15:16
 *  @Description: random select from serviceList.
 */

public class RandomLoadbalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest request) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}

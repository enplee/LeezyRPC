package github.enplee.loadbalance;

import github.enplee.remoting.dto.RpcRequest;

import java.util.List;

/**
 *  @author: leezy
 *  @Date: 2021/5/20 15:10
 *  @Description:
 */
public interface LoadBalance {

    String selectServiceAddress(List<String> serviceAddersses, RpcRequest request);

}

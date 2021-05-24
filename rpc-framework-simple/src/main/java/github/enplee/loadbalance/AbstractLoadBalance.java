package github.enplee.loadbalance;

import github.enplee.remoting.dto.RpcRequest;

import java.util.List;


public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceAddersses, RpcRequest request) {
        // do common pre-check
        if(serviceAddersses == null || serviceAddersses.size()==0){
            return null;
        }
        if(serviceAddersses.size() == 1) {
            return serviceAddersses.get(0);
        }
        return doSelect(serviceAddersses,request);
    }

    protected abstract String doSelect(List<String> serviceAddresses, RpcRequest request);
}

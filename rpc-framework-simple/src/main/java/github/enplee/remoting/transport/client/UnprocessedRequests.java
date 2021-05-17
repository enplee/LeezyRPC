package github.enplee.remoting.transport.client;

import github.enplee.remoting.dto.RpcResponce;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 14:33
 *  @Description: contain the  request future
 */

public class UnprocessedRequests {
    private static final Map<String, CompletableFuture<RpcResponce<Object>>> UNPROCESSED_PRESPONSE_FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId,CompletableFuture<RpcResponce<Object>> future){
        UNPROCESSED_PRESPONSE_FUTURES.put(requestId,future);
    }

    public void complete(RpcResponce<Object> rpcResponce){
        CompletableFuture<RpcResponce<Object>> future = UNPROCESSED_PRESPONSE_FUTURES.remove(rpcResponce.getRequstId());
        if(future != null){
            future.complete(rpcResponce);
        }else {
            throw new IllegalStateException();
        }
    }
}

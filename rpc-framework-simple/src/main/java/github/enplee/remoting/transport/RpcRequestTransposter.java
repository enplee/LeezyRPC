package github.enplee.remoting.transport;

import github.enplee.remoting.dto.RpcRequest;
import github.enplee.remoting.dto.RpcResponce;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 21:05
 *  @Description: 发送请求并接收结果
 */
public interface RpcRequestTransposter {

    Object sendRpcRequest(RpcRequest request);
}

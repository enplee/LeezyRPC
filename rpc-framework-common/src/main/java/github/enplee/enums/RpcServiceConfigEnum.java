package github.enplee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  RpcServiceConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDERSS("rpc.zookeeper.address");

    private final String propertyValue;
}

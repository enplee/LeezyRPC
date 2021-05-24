package github.enplee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum  RpcErrorMessageEnum {

    SERVICE_CAN_NOT_BE_FOUND("没有找到指定服务"),
    SERVICE_INVOKATION_FAILURE("服务调用失败"),
    REQUEST_NOT_MATCH_REQUEST("服务返回错误！请求与相应不匹配");
    private final String message;
}

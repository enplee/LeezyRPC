package github.enplee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCodeEnum {

    SUCCESS(200,"The remote process call is successful"),
    FAIL(500,"The remote process call is fail");
    private final int code;
    private final String message;
}

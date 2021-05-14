package github.enplee.remoting.dto;

import github.enplee.enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 16:11
 *  @Description:
 *  1. resp/req通过相同的requestId关联
 *  2. resp附带调用succ/fail描述 code&message
 *  3. 调用成功且有返回值，data进行携带
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponce<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    private String requstId;
    // succ/fail code
    private Integer code;
    // succ/fail description message
    private String message;
    private T data;

    public static <T> RpcResponce<T> success(T data, String requstId){
        RpcResponce<T> responce = new RpcResponce<>();
        responce.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        responce.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        responce.setRequstId(requstId);
        if(data != null) {
            responce.setData(data);
        }
        return responce;
    }

    public static <T> RpcResponce<T> fail(RpcResponseCodeEnum codeEnum) {
        RpcResponce<T> responce = new RpcResponce<>();
        responce.setCode(codeEnum.getCode());
        responce.setMessage(codeEnum.getMessage());
        return responce;
    }

}
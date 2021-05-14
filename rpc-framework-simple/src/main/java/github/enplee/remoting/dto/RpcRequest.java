package github.enplee.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 16:11
 *  @Description:
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L; // 保证升级前后的兼容性，保证可解析
    private String requestId;
    private String serviceName;
    private String methordName;
    private Object[] args;
    private Class<?>[] argsType;
    // 区分服务不同的实现
    private String version;
    private String group;

}
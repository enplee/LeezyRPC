package github.enplee.remoting.dto;

import lombok.*;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 16:29
 *  @Description: 定义消息头，Req和resp共用相同的消息头定义。
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcMessage {
    // message type
    private byte messageType;
    // serialization type
    private byte codec;
    // compress type
    private byte compress;
    // request ID
    private int requestId;
    // message Body (RpcReq,RpcResp)
    private Object body;
}

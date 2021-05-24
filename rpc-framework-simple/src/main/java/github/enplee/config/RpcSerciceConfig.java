package github.enplee.config;

import lombok.*;

/**
 *  @author: leezy
 *  @Date: 2021/5/20 15:20
 *  @Description: 用来描述service的对象，包括 version - group
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcSerciceConfig {


    private String version = "";
    /**
     *  当一个接口有多个实现的时候，用来区别不同的实现
     */
    private String group = "";

    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    private String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}

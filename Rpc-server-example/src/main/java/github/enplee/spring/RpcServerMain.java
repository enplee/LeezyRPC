package github.enplee.spring;

import github.enplee.remoting.transport.server.RpcNettyServer;
import github.enplee.anotation.RpcScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *  @author: leezy
 *  @Date: 2021/5/26 16:35
 *  @Description: spring启动配置类
 */

@RpcScan(basePackage = {"github.enplee"})
@Slf4j
public class RpcServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(RpcServerMain.class);
        RpcNettyServer rpcNettyServer = (RpcNettyServer)configApplicationContext.getBean("rpcNettyServer");
        rpcNettyServer.start();
    }
}

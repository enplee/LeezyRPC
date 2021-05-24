package github.enplee.registry.zk.utils;

import github.enplee.Utils.PropertiesFileUtil;
import github.enplee.enums.RpcServiceConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *  @author: leezy
 *  @Date: 2021/5/21 15:30
 *  @Description: provide curator frameWork operate Util
 */
@Slf4j
public class CuratorUtil {

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;

    private static CuratorFramework zkClient;
    private static Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap();
    private static Set<String> REGISTED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static String DEFAULT_ZOOKEEPER_ADDR = "127.0.0.1:2181";
    public static String PRC_REGISTY_ROOT = "/leezy_RPC";


    // 获取zkClient 服务端地址应该是可配置的
    public static CuratorFramework getZkClient() {
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcServiceConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String zkAdderss = DEFAULT_ZOOKEEPER_ADDR;
        if(properties !=null && properties.getProperty(RpcServiceConfigEnum.ZK_ADDERSS.getPropertyValue()) !=null) {
            zkAdderss = properties.getProperty(RpcServiceConfigEnum.ZK_ADDERSS.getPropertyValue());
        }

        if(zkClient !=null && zkClient.getState()== CuratorFrameworkState.STARTED) {
            return zkClient;
        }

        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .retryPolicy(retry)
                .connectString(zkAdderss)
                .build();
        zkClient.start();
        // connect test
        try {
            // wait 30s until connect to the zookeeper
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)){
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkClient;
    }

    // zk 节点的创建
    public static void createPersistentNode(CuratorFramework zkClient, String path){
        try {
            if(REGISTED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null){
                log.info("the node already exists. The node is [{}]",path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("the node was created successfully. The node is: [{}]",path);
            }
            REGISTED_PATH_SET.add(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // zk 节点服务查询
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if(SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)){
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> res = null;
        String servicePath = PRC_REGISTY_ROOT + "/" + rpcServiceName;

        try {
            List<String> serviceAddress = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(servicePath,serviceAddress);
            // 为服务注册监听
            registerWatcher(zkClient,rpcServiceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    // zk 服务的撤销：server本身注销 service服务注销
    public static void clearRegisty(CuratorFramework zkClient, InetSocketAddress inetSocketAddress){
        REGISTED_PATH_SET.stream().parallel().forEach(p -> {
            if (p.endsWith(inetSocketAddress.toString())){
                try {
                    zkClient.delete().forPath(p);
                    REGISTED_PATH_SET.remove(p);
                } catch (Exception e) {
                    log.error("clear registry for path [{}] fail", p);
                }
            }
        });
        log.info("All registered services on the server are cleared:[{}]", REGISTED_PATH_SET.toString());
    }

    public static void cancelService(CuratorFramework zkClient, String path) {

    }
    // zk 为服务注册监视器
    public static void registerWatcher(CuratorFramework zkClient, String serviceName) throws Exception {
        String servicePath = PRC_REGISTY_ROOT + "/" + serviceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        // 监听改动，触发更新，那么将新改动重新load到
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(serviceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }
}

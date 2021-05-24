package github.enplee.zkTest;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

public class zkTest {
    public static final int BASE_SLEEP_TIME = 10000;
    public static final int MAX_RETRES = 3;
    @Test
    public void zkClientTest() throws Exception {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRES);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("192.168.178.111:2181")
                .retryPolicy(retry)
                .build();
        zkClient.start();
        System.out.println(zkClient.getState());


        Stat stat = zkClient.checkExists().forPath("/zookeeper");

        //zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/node1/0001","java".getBytes());
//        byte[] bytes = zkClient.getData().forPath("/node1/0001");
//        System.out.println(String.valueOf(bytes));

    }
}

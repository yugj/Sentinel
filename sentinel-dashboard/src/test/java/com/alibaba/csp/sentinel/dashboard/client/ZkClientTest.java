package com.alibaba.csp.sentinel.dashboard.client;

import com.alibaba.csp.sentinel.dashboard.rule.zookeeper.ZookeeperConfigUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * @author yugj
 * @date 2021/5/31 15:37.
 */
public class ZkClientTest {

    @Test
    public void createTest() throws Exception {
        String zkNodes = "zookeeper.hktrd.cn:2181";

        CuratorFramework zkClient =
                CuratorFrameworkFactory.newClient(zkNodes,
                        new ExponentialBackoffRetry(ZookeeperConfigUtil.SLEEP_TIME, ZookeeperConfigUtil.RETRY_TIMES));
        zkClient.start();

        String path = "/sentinel/sentinel_system_config/zuul-sentinel-2";
        zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);

        zkClient.close();
    }

}

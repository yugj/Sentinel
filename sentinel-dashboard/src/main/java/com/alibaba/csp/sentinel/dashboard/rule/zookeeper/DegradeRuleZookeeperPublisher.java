package com.alibaba.csp.sentinel.dashboard.rule.zookeeper;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yugj
 * @date 2021/5/31 13:52.
 */
@Component("degradeRuleZookeeperPublisher")
public class DegradeRuleZookeeperPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    @Autowired
    private CuratorFramework zkClient;
    @Resource(name = "degradeRuleEntityEncoder")
    private Converter<List<DegradeRuleEntity>, String> converter;

    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {

        AssertUtil.notEmpty(app, "app name cannot be empty");

        String path = ZookeeperConfigUtil.getPath(app, ZookeeperConfigUtil.DEGRADE_ROOT_PATH);
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }

        byte[] data = CollectionUtils.isEmpty(rules) ? "[]".getBytes() : converter.convert(rules).getBytes();
        zkClient.setData().forPath(path, data);
    }
}

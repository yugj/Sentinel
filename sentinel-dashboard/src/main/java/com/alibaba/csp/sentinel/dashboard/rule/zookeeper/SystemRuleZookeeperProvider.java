package com.alibaba.csp.sentinel.dashboard.rule.zookeeper;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yugj
 * @date 2021/5/31 14:25.
 */
@Component("systemRuleZookeeperProvider")
public class SystemRuleZookeeperProvider implements DynamicRuleProvider<List<SystemRuleEntity>> {

    @Autowired
    private CuratorFramework zkClient;
    @Resource(name = "systemRuleEntityDecoder")
    private Converter<String, List<SystemRuleEntity>> converter;

    @Override
    public List<SystemRuleEntity> getRules(String appName) throws Exception {
        String zkPath = ZookeeperConfigUtil.getPath(appName, ZookeeperConfigUtil.SYSTEM_ROOT_PATH);
        byte[] bytes = zkClient.getData().forPath(zkPath);
        if (null == bytes || bytes.length == 0) {
            return new ArrayList<>();
        }
        String s = new String(bytes);

        return converter.convert(s);
    }
}

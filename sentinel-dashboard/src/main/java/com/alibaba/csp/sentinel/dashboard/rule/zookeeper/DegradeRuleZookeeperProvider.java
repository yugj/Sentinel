package com.alibaba.csp.sentinel.dashboard.rule.zookeeper;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
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
 * @date 2021/5/31 13:43.
 */
@Component("degradeRuleZookeeperProvider")
public class DegradeRuleZookeeperProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

    @Autowired
    private CuratorFramework zkClient;
    @Resource(name = "degradeRuleEntityDecoder")
    private Converter<String, List<DegradeRuleEntity>> converter;

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        String zkPath = ZookeeperConfigUtil.getPath(appName, ZookeeperConfigUtil.DEGRADE_ROOT_PATH);
        byte[] bytes = zkClient.getData().forPath(zkPath);
        if (null == bytes || bytes.length == 0) {
            return new ArrayList<>();
        }
        String s = new String(bytes);

        return converter.convert(s);
    }
}

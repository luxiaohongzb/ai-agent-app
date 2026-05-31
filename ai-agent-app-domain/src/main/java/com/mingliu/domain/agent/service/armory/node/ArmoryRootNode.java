package com.mingliu.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;

import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.service.armory.AbstractArmorySupport;
import com.mingliu.domain.agent.service.armory.business.data.ILoadDataStrategy;
import com.mingliu.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: RootNode
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.armory.node
 * @Date 2025/8/11 21:43
 * @description:
 */
@Service
@Slf4j
public class ArmoryRootNode extends AbstractArmorySupport {
    @Resource
    AiClientApiNode aiClientApiNode;
    //当构造函数参数是一个Map，并且Map的value类型是某个接口时，Spring会自动收集所有实现该接口的Bean，并以其Bean名称作为key创建Map注入
    private final Map<String, ILoadDataStrategy> loadDataStrategyMap;

    public ArmoryRootNode(Map<String, ILoadDataStrategy> loadDataStrategyMap) {
        this.loadDataStrategyMap = loadDataStrategyMap;
    }

    @Override
    protected void multiThread(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        String commandType = armoryCommandEntity.getCommandType();
        ILoadDataStrategy iLoadDataStrategy = loadDataStrategyMap.get(armoryCommandEntity.getLoadDataStrategy());
        iLoadDataStrategy.loadData(armoryCommandEntity,dynamicContext);
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，数据加载节点{}", JSON.toJSONString(armoryCommandEntity));
        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return aiClientApiNode;
    }
}

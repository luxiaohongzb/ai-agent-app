package com.mingliu.domain.agent.service.preheat;

import com.mingliu.domain.agent.adapter.repository.IAgentRepository;
import com.mingliu.domain.agent.model.entity.AiAgentEngineStarterEntity;
import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.model.valobj.enums.AiAgentEnumVO;
import com.mingliu.domain.agent.service.IAiAgentPreheatService;
import com.mingliu.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 装配服务
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-05 09:12
 */
@Slf4j
@Service
public class AiAgentPreheatService implements IAiAgentPreheatService {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;
    @Resource
    private IAgentRepository repository;

    @Override
    public void preheat() throws Exception {
        List<String> aiClientIds = repository.queryAiClientIds();
        log.info(aiClientIds.toString());
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> handler = defaultArmoryStrategyFactory.strategyHandler();
        handler.apply(ArmoryCommandEntity.builder()
                .commandIdList(aiClientIds)
                .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                .build(), new DefaultArmoryStrategyFactory.DynamicContext());
    }

    @Override
    public void preheat(String aiClientId) throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> handler = defaultArmoryStrategyFactory.strategyHandler();
        handler.apply(ArmoryCommandEntity.builder()
                .commandIdList(Collections.singletonList(aiClientId))
                .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                .build(), new DefaultArmoryStrategyFactory.DynamicContext());
    }

    @Override
    public void preheat(List<String> aiClientIds) throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> handler = defaultArmoryStrategyFactory.strategyHandler();
        handler.apply(ArmoryCommandEntity.builder()
                .commandIdList(aiClientIds)
                .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                .build(), new DefaultArmoryStrategyFactory.DynamicContext());
    }

}

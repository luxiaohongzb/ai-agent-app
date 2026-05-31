package com.mingliu.domain.agent.service.execute.auto.step;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.mingliu.domain.agent.model.entity.ExecuteCommandEntity;
import com.mingliu.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import com.mingliu.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Title: RootNode
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.execute.auto.step.factory
 * @Date 2025/8/14 22:45
 * @description: 加载根节点
 */


@Service
public class ExecuteRootNode extends AbstractExecuteSupport{
    @Resource
    private Step1AnalyzerNode step1AnalyzerNode;
    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("=== 动态多轮执行测试开始 ====");
        log.info("用户输入: {}", executeCommandEntity.getMessage());
        log.info("最大执行步数: {}", executeCommandEntity.getMaxStep());
        log.info("会话ID: {}", executeCommandEntity.getSessionId());
        Map<String, AiAgentClientFlowConfigVO> stringAiAgentClientFlowConfigVOMap = repository.queryAiAgentClientFlowConfig(executeCommandEntity.getAiAgentId());
        dynamicContext.setAiAgentClientFlowConfigVOMap(stringAiAgentClientFlowConfigVOMap);
        dynamicContext.setExecutionHistory(new StringBuilder());
        dynamicContext.setMaxStep(executeCommandEntity.getMaxStep());
        dynamicContext.setCurrentTask(executeCommandEntity.getMessage());
        return router(executeCommandEntity,dynamicContext);

    }


    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step1AnalyzerNode;

    }
}

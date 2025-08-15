package com.mingliu.domain.agent.service.execute.auto.step.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.mingliu.domain.agent.model.entity.ExecuteCommandEntity;
import com.mingliu.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import com.mingliu.domain.agent.service.execute.auto.step.ExecuteRootNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: DefaultAutoAgentExecuteStrategyFactory
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.execute.auto.step.factory
 * @Date 2025/8/14 22:44
 * @description: 默认装配工厂
 */

@Component
public class DefaultAutoAgentExecuteStrategyFactory {

    private final ExecuteRootNode executeRootNode;

    public DefaultAutoAgentExecuteStrategyFactory(ExecuteRootNode executeRootNode) {
        this.executeRootNode = executeRootNode;
    }

    public StrategyHandler<ExecuteCommandEntity, DynamicContext, String> armoryStrategyHandler(){
        return executeRootNode;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{

        // 任务执行步骤
        private int step = 1;

        // 最大任务步骤
        private int maxStep = 1;

        private StringBuilder executionHistory;

        private String currentTask;

        boolean isCompleted = false;

        private Map<String, AiAgentClientFlowConfigVO> aiAgentClientFlowConfigVOMap;

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }
    }
}

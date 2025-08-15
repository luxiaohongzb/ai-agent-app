package com.mingliu.domain.agent.service.armory.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;

import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.service.armory.node.ArmoryRootNode;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * @Title: DefaultArmoryStrategyFactory
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.armory.factory
 * @Date 2025/8/11 21:46
 * @description:
 */

@Service
public class DefaultArmoryStrategyFactory {
    @Resource
    private ApplicationContext applicationContext;
    private final ArmoryRootNode rootNode;
    public DefaultArmoryStrategyFactory(ArmoryRootNode rootNode) {
        this.rootNode = rootNode;
    }
    public StrategyHandler<ArmoryCommandEntity, DynamicContext, String> strategyHandler() {
        return rootNode;
    }

    public ChatClient chatClient(Long clientId) {
        return (ChatClient) applicationContext.getBean("ChatClient_" + clientId);
    }

    public ChatModel chatModel(Long modelId) {
        return (ChatModel) applicationContext.getBean("AiClientModel_" + modelId);
    }

    public static class DynamicContext{
        private int level;
        private Map<String,Object> dataObjects = new HashMap<>();
        public <T> void setValue(String key,T value){
            dataObjects.put(key,value);
        }
        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }
    }
}

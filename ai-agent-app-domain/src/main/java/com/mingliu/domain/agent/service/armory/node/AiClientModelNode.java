package com.mingliu.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.model.valobj.AiClientModelVO;
import com.mingliu.domain.agent.model.valobj.enums.AiAgentEnumVO;
import com.mingliu.domain.agent.service.armory.AbstractArmorySupport;
import com.mingliu.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import io.modelcontextprotocol.client.McpSyncClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: AiClientModelNode
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.armory.node
 * @Date 2025/8/12 20:38
 * @description:
 */


@Service
@Slf4j
public class AiClientModelNode extends AbstractArmorySupport {

    @Resource
    private AiClientAdvisorNode aiClientAdvisorNode;
    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建节点，Mode 对话模型{}", JSON.toJSONString(armoryCommandEntity));
        List<AiClientModelVO> aiClientModelList = dynamicContext.getValue(dataName());
        if (aiClientModelList == null || aiClientModelList.isEmpty()) {
            log.warn("没有需要被初始化的 ai client model");
            return router(armoryCommandEntity, dynamicContext);
        }
        for (AiClientModelVO modelVO : aiClientModelList) {
            // 获取当前模型关联的 API Bean 对象
            OpenAiApi openAiApi = getBean(AiAgentEnumVO.AI_CLIENT_API.getBeanName(modelVO.getApiId()));
            if (null == openAiApi) {
                throw new RuntimeException("mode 2 api is null");
            }

            //获取当前模型所关联的tool Mcp bean对象
            List<McpSyncClient> mcpSyncClients = new ArrayList<>();
            for( String toolMcpId: modelVO.getToolMcpIds()){
                McpSyncClient mcpSyncClient = getBean(AiAgentEnumVO.AI_CLIENT_TOOL_MCP.getBeanName(toolMcpId));
                mcpSyncClients.add(mcpSyncClient);
            }
            OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                    .openAiApi(openAiApi)
                    .defaultOptions(
                            OpenAiChatOptions.builder()
                                    .model(modelVO.getModelName())
                                    .toolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients).getToolCallbacks())
                                    .build()
                    )
                    .build();
            registerBean(beanName(modelVO.getModelId()), OpenAiChatModel.class, openAiChatModel);
        }
        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    protected String beanName(String id) {
        return AiAgentEnumVO.AI_CLIENT_MODEL.getBeanName(id);
    }

    @Override
    protected String dataName() {
        return AiAgentEnumVO.AI_CLIENT_MODEL.getDataName();
    }
    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return aiClientAdvisorNode;
    }
}

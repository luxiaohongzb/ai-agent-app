package com.mingliu.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.model.valobj.AiClientApiVO;
import com.mingliu.domain.agent.model.valobj.AiClientModelVO;
import com.mingliu.domain.agent.model.valobj.enums.AiAgentEnumVO;
import com.mingliu.domain.agent.service.armory.AbstractArmorySupport;
import com.mingliu.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: AiClientApiNode
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.armory.node
 * @Date 2025/8/11 23:28
 * @description:
 */


@Slf4j
@Service
public class AiClientApiNode extends AbstractArmorySupport {
    @Resource
    AiClientToolMcpNode aiClientToolMcpNode;
    @Override
    protected String doApply(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，API 构建节点 {}", JSON.toJSONString(requestParameter));
        List<AiClientApiVO> aiClientApiList = dynamicContext.getValue(dataName());

        if(null == aiClientApiList || aiClientApiList.isEmpty()){
            log.info("无任何需要初始化的ai client api");
            return router(requestParameter, dynamicContext);
        }
        //拿到api参数
        for (AiClientApiVO aiClientApiVO:aiClientApiList){
            OpenAiApi openAiApi = OpenAiApi.builder()
                    .baseUrl(aiClientApiVO.getBaseUrl())
                    .apiKey(aiClientApiVO.getApiKey())
                    .completionsPath(aiClientApiVO.getCompletionsPath())
                    .embeddingsPath(aiClientApiVO.getEmbeddingsPath())
                    .build();
            registerBean(beanName(aiClientApiVO.getApiId()),OpenAiApi.class,openAiApi);
        }
        return router(requestParameter,dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return aiClientToolMcpNode;

    }
    @Override
    protected String beanName(String beanId) {
        return AiAgentEnumVO.AI_CLIENT_API.getBeanName(beanId);
    }


    @Override
    protected String dataName() {
        return AiAgentEnumVO.AI_CLIENT_API.getDataName();
    }
}

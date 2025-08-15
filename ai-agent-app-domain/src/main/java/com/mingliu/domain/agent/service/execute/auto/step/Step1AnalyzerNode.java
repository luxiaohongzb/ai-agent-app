package com.mingliu.domain.agent.service.execute.auto.step;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.mingliu.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import com.mingliu.domain.agent.model.entity.ExecuteCommandEntity;
import com.mingliu.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import com.mingliu.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.mingliu.domain.agent.model.valobj.enums.AiClientTypeEnumVO;
import java.util.Map;

/**
 * @Title: Step1AnalyzerNode
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.execute.auto.step
 * @Date 2025/8/14 23:19
 * @description:
 */


@Service
public class Step1AnalyzerNode extends AbstractExecuteSupport{
    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n🎯 === 执行第 {} 步 ===", dynamicContext.getStep());
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.TASK_ANALYZER_CLIENT.getCode());
        // 第一阶段：任务分析
        log.info("\n📊 阶段1: 任务状态分析");
        String analysisPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(),
                executeCommandEntity.getMessage(),
                dynamicContext.getStep(),
                dynamicContext.getMaxStep(),
                !dynamicContext.getExecutionHistory().isEmpty() ? dynamicContext.getExecutionHistory().toString() : "[首次执行]",
                dynamicContext.getCurrentTask()
        );
        ChatClient chatClientByClientId = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());

        String analysisResult = chatClientByClientId
                .prompt(analysisPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, executeCommandEntity.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1024))
                .call().content();

        assert analysisResult != null;
        parseAnalysisResult(dynamicContext, analysisResult, executeCommandEntity.getSessionId());

        dynamicContext.setValue("analysisResult", analysisResult);

        // 检查是否已完成
        if (analysisResult.contains("任务状态: COMPLETED") ||
                analysisResult.contains("完成度评估: 100%")) {
            dynamicContext.setCompleted(true);
            log.info("✅ 任务分析显示已完成！");
            return router(executeCommandEntity, dynamicContext);
        }

        return router(executeCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        if(dynamicContext.isCompleted() || dynamicContext.getStep() > dynamicContext.getMaxStep()){
            return getBean("step4LogExecutionSummaryNode");
        }
        else{
            // 否则继续执行下一步
            return getBean("step2PrecisionExecutorNode");
        }
    }

    private void parseAnalysisResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, String analysisResult, String sessionId) {
        int step = dynamicContext.getStep();
        log.info("\n📊 === 第 {} 步分析结果 ===", step);

        String[] lines = analysisResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("任务状态分析:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_status";
                sectionContent = new StringBuilder();
                log.info("\n🎯 任务状态分析:");
                continue;
            } else if (line.contains("执行历史评估:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_history";
                sectionContent = new StringBuilder();
                log.info("\n📈 执行历史评估:");
                continue;
            } else if (line.contains("下一步策略:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_strategy";
                sectionContent = new StringBuilder();
                log.info("\n🚀 下一步策略:");
                continue;
            } else if (line.contains("完成度评估:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_progress";
                sectionContent = new StringBuilder();
                String progress = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n📊 完成度评估: {}", progress);
                sectionContent.append(line).append("\n");
                continue;
            } else if (line.contains("任务状态:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_task_status";
                sectionContent = new StringBuilder();
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("COMPLETED")) {
                    log.info("\n✅ 任务状态: 已完成");
                } else {
                    log.info("\n🔄 任务状态: 继续执行");
                }
                sectionContent.append(line).append("\n");
                continue;
            }

            // 收集当前section的内容
            if (!currentSection.isEmpty()) {
                sectionContent.append(line).append("\n");
                switch (currentSection) {
                    case "analysis_status":
                        log.info("   📋 {}", line);
                        break;
                    case "analysis_history":
                        log.info("   📊 {}", line);
                        break;
                    case "analysis_strategy":
                        log.info("   🎯 {}", line);
                        break;
                    default:
                        log.info("   📝 {}", line);
                        break;
                }
            }
        }

        // 发送最后一个section的内容
        sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
    }

    /**
     * 发送分析阶段细分结果到流式输出
     */
    private void sendAnalysisSubResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext,
                                       String subType, String content, String sessionId) {
        if (!subType.isEmpty() && !content.isEmpty()) {
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createAnalysisSubResult(
                    dynamicContext.getStep(), subType, content, sessionId);
            sendSseResult(dynamicContext, result);
        }
    }
}

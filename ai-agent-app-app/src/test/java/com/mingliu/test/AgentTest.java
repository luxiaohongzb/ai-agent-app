package com.mingliu.test;


import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.model.entity.ExecuteCommandEntity;
import com.mingliu.domain.agent.model.valobj.enums.AiAgentEnumVO;
import com.mingliu.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import com.mingliu.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@SpringBootTest()
@RunWith(SpringRunner.class)
public class AgentTest {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;

    @Resource
    DefaultAutoAgentExecuteStrategyFactory defaultAutoAgentExecuteStrategyFactory;
    @Resource
    private ApplicationContext applicationContext;
//
//    @Value("classpath:data/dog.png")
//    private org.springframework.core.io.ResourceimageResource im;


    private ChatClient chatClient;
    @Before
    public void init() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> strategiedHandler = defaultArmoryStrategyFactory.strategyHandler();
        strategiedHandler.apply(ArmoryCommandEntity.builder()
                .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                .commandIdList(Arrays.asList("3101", "3102", "3103","3104"))
                .build(),new DefaultArmoryStrategyFactory.DynamicContext());
        ChatClient chatClient = (ChatClient) applicationContext.getBean(AiAgentEnumVO.AI_CLIENT.getBeanName("3101"));

        log.info("客户端构建：{}", chatClient);
    }
    @Test
    public void autoAgent() throws Exception {
        StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> executeHandler
                = defaultAutoAgentExecuteStrategyFactory.armoryStrategyHandler();

        ExecuteCommandEntity executeCommandEntity = new ExecuteCommandEntity();
        executeCommandEntity.setAiAgentId("3");
        executeCommandEntity.setMessage("搜索小傅哥，技术项目列表。编写成一份文档，说明不同项目的学习目标，以及不同阶段的伙伴应该学习哪个项目。");
        executeCommandEntity.setSessionId("session-id-" + System.currentTimeMillis());
        executeCommandEntity.setMaxStep(3);

        String apply = executeHandler.apply(executeCommandEntity, new DefaultAutoAgentExecuteStrategyFactory.DynamicContext());
        log.info("测试结果:{}", apply);
    }
    @Test
    public void testAiClientApiNode() throws Exception {


//        String content = chatClient.
//
//                prompt(Prompt.builder()
//                .messages(new UserMessage(
//                        "搜索xfg博客"))
//                .build()).
//                system(s->s.param("current_date", LocalDate.now().toString()))
//                .call().content();
//
//        log.info("测试结果(call):{}", content);

    }
        @Test
        public void test() {
            McpSyncClient mcpSyncClient = sseMcpClient();

//            String res = chatClient.prompt(Prompt.builder().messages(new UserMessage("搜索小傅哥技术博客有哪些项目")).build()).call().content();
//            log.info("测试结果:{}", res);
        }

 
        public McpSyncClient sseMcpClient() {
            HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://appbuilder.baidu.com/v2/ai_search/mcp/")
                    .sseEndpoint("sse?api_key=Bearer+bce-v3/ALTAK-JIu2dvdMXs03ndtiR5rCf/79d75ceb505278e8d074b3a34eddfd88e9029ad8")
                    .build();

            McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();
            var init_sse = mcpSyncClient.initialize();
            log.info("Tool SSE MCP Initialized {}", init_sse);

            return mcpSyncClient;


        }
}
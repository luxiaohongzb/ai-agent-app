package com.mingliu.trigger.http.agent;

import com.mingliu.api.IAiAgentService;
import com.mingliu.api.dto.AutoAgentRequestDTO;
import com.mingliu.api.response.Response;
import com.mingliu.domain.agent.model.entity.ExecuteCommandEntity;
import org.springframework.ai.chat.model.ChatResponse;
import com.alibaba.fastjson.JSON;
import com.mingliu.domain.agent.service.IAiAgentChatService;
import com.mingliu.domain.agent.service.IAiAgentPreheatService;
import com.mingliu.domain.agent.service.IAiAgentRagService;
import com.mingliu.domain.agent.service.execute.auto.step.IExecuteStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * AutoAgent 自动智能对话体
 *
 * @author xiaofuge bugstack.cn @小傅哥
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/agent")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@Tag(name = "AutoAgent流式接口", description = "提供流式Agent对话接口")
public class AiAgentController implements IAiAgentService {

    @Resource(name = "autoAgentExecuteStrategy")
    private IExecuteStrategy autoAgentExecuteStrategy;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private IAiAgentChatService aiAgentChatService;

    @Resource
    private IAiAgentRagService aiAgentRagService;

    @Resource
    private IAiAgentPreheatService aiAgentPreheatService;


    @Operation(summary = "AutoAgent流式执行", description = "提供流式Agent对话接口")
    @RequestMapping(value = "auto_agent", method = RequestMethod.POST)
    public ResponseBodyEmitter autoAgent(@RequestBody AutoAgentRequestDTO request, HttpServletResponse response) {
        log.info("AutoAgent流式执行请求开始，请求信息：{}", JSON.toJSONString(request));
        
        try {
            // 设置SSE响应头
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Connection", "keep-alive");

            // 1. 创建流式输出对象
            ResponseBodyEmitter emitter = new ResponseBodyEmitter(Long.MAX_VALUE);
            
            // 2. 构建执行命令实体
            ExecuteCommandEntity executeCommandEntity = ExecuteCommandEntity.builder()
                    .aiAgentId(request.getAiAgentId())
                    .message(request.getMessage())
                    .sessionId(request.getSessionId())
                    .maxStep(request.getMaxStep())
                    .build();
            
            // 3. 异步执行AutoAgent
            threadPoolExecutor.execute(() -> {
                try {
                    autoAgentExecuteStrategy.execute(executeCommandEntity, emitter);
                } catch (Exception e) {
                    log.error("AutoAgent执行异常：{}", e.getMessage(), e);
                    try {
                        emitter.send("执行异常：" + e.getMessage());
                    } catch (Exception ex) {
                        log.error("发送异常信息失败：{}", ex.getMessage(), ex);
                    }
                } finally {
                    try {
                        emitter.complete();
                    } catch (Exception e) {
                        log.error("完成流式输出失败：{}", e.getMessage(), e);
                    }
                }
            });
            
            return emitter;

        } catch (Exception e) {
            log.error("AutoAgent请求处理异常：{}", e.getMessage(), e);
            ResponseBodyEmitter errorEmitter = new ResponseBodyEmitter();
            try {
                errorEmitter.send("请求处理异常：" + e.getMessage());
                errorEmitter.complete();
            } catch (Exception ex) {
                log.error("发送错误信息失败：{}", ex.getMessage(), ex);
            }
            return errorEmitter;
        }
    }
    /**
     * AI代理预热
     * curl --request GET \
     *   --url 'http://localhost:8091/ai-agent-station/api/v1/ai/agent/preheat?aiAgentId=1'
     */
    @RequestMapping(value = "preheat", method = RequestMethod.GET)
    public ResponseEntity<Boolean> preheat(@RequestParam("aiAgentId") String aiClientId) {
        try {
            log.info("预热装配 AiAgent {}", aiClientId);
            aiAgentPreheatService.preheat(aiClientId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            log.error("预热装配 AiAgent {}", aiClientId,e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * AI代理执行方法，用于处理用户输入的消息并返回AI代理的回复
     * <p>
     * 示例请求:
     * curl -X GET "http://localhost:8091/ai-agent-station/api/v1/ai/agent/chat_agent?aiAgentId=1&message=生成一篇文章" -H "Content-Type: application/json"
     *
     * @param aiAgentId AI代理ID，用于标识使用哪个AI代理
     * @param message   用户输入的消息内容
     * @return AI代理的回复内容
     */
    @RequestMapping(value = "chat_agent", method = RequestMethod.GET)
    public ResponseEntity<String> chatAgent(@RequestParam("aiAgentId") String aiAgentId, @RequestParam("message") String message) {
        try {
            log.info("AiAgent 智能体对话，请求 {} {}", aiAgentId, message);
            String content = aiAgentChatService.aiAgentChat(aiAgentId, message);
            ResponseEntity<String> response = ResponseEntity.ok(content);
            log.info("AiAgent 智能体对话，结果 {} {}", aiAgentId, JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("AiAgent 智能体对话，异常 {} {}", aiAgentId, message, e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * curl http://localhost:8091/ai-agent-station/api/v1/ai/agent/chat_stream?modelId=1&ragId=1&message=hi
     */
//    @RequestMapping(value = "chat_stream", method = RequestMethod.GET)
//    public Flux<ChatResponse> chatStream(@RequestParam("aiAgentId") String aiAgentId, @RequestParam("ragId") String ragId, @RequestParam("message") String message) {
//        try {
//            log.info("AiAgent 智能体对话(stream)，请求 {} {} {}", aiAgentId, ragId, message);
//            return aiAgentChatService.aiAgentChatStream(aiAgentId, ragId, message);
//        } catch (Exception e) {
//            log.error("AiAgent 智能体对话(stream)，失败 {} {} {}", aiAgentId, ragId, message, e);
//            throw e;
//        }
//    }

    /**
     * http://localhost:8091/ai-agent-station/api/v1/ai/agent/file/upload
     */
    @RequestMapping(value = "file/upload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<Boolean> uploadRagFile(@RequestParam("name") String name, @RequestParam("tag") String tag, @RequestParam("files") List<MultipartFile> files) {
        try {
            log.info("上传知识库，请求 {}", name);
            aiAgentRagService.storeRagFile(name, tag, files);
            ResponseEntity<Boolean> response = ResponseEntity.ok(true);
            log.info("上传知识库，结果 {} {}", name, JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("上传知识库，异常 {}", name, e);
            return ResponseEntity.status(500).build();
        }
    }

}
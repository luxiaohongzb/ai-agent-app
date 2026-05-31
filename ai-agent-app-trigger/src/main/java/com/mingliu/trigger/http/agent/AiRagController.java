package com.mingliu.trigger.http.agent;

import com.alibaba.fastjson.JSON;
import com.mingliu.domain.agent.service.IAiAgentRagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Title: AiRagController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.agent
 * @Date 2025/8/21 23:54
 * @description:
 */


@Slf4j
@RestController
@RequestMapping("/api/v1/agent/rag")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@Tag(name = "RAG相关管理RAG知识库相关", description = "提供RAG知识库相关接口")
public class AiRagController {
    @Resource
    private IAiAgentRagService aiAgentRagService;

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

    @RequestMapping(value = "analyzeGitRepository", method = RequestMethod.POST)
    public ResponseEntity<String> analyzeGitRepository(@RequestParam("repoUrl") String repoUrl, @RequestParam("userName") String userName, @RequestParam("token") String token,@RequestParam("branch") String branch) {
        try {
            log.info(repoUrl);
            log.info(userName);
            log.info("git仓库解析，请求仓库:{}", repoUrl);
            aiAgentRagService.analyzeGitRepository(repoUrl, userName, token,branch);
            ResponseEntity<Boolean> response = ResponseEntity.ok(true);
            log.info("上传知识库，结果 {}: {}", repoUrl, JSON.toJSONString(response));
            return ResponseEntity.ok("上传成功");
        } catch (Exception e) {
            log.error("上传知识库，异常 {}", repoUrl, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}

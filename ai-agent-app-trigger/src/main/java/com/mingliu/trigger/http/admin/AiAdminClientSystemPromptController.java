package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientSystemPromptDao;
import com.mingliu.infrastructure.dao.po.AiClientSystemPrompt;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 系统提示词管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 15:55
 */
@Tag(name = "系统提示词管理", description = "系统提示词相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/system/prompt/")
public class AiAdminClientSystemPromptController {

    @Resource
    private IAiClientSystemPromptDao aiClientSystemPromptDao;

    @Operation(summary = "查询所有系统提示词配置", description = "获取所有系统提示词配置列表")
    @RequestMapping(value = "queryAllSystemPromptConfig", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientSystemPrompt>> queryAllSystemPromptConfig(
            @Parameter(description = "查询条件", required = true) @RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize(),  request.getOrderBy());
            
            List<AiClientSystemPrompt> systemPromptList = aiClientSystemPromptDao.queryAll();
            
            // 包装分页结果
            PageInfo<AiClientSystemPrompt> pageInfo = new PageInfo<>(systemPromptList);
            PageResponse<AiClientSystemPrompt> response = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询系统提示词列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询系统提示词", description = "根据系统提示词ID查询详细信息")
    @RequestMapping(value = "querySystemPromptById", method = RequestMethod.GET)
    public ResponseEntity<AiClientSystemPrompt> querySystemPromptById(
            @Parameter(description = "系统提示词ID", required = true) @RequestParam("id") String id) {
        try {
            AiClientSystemPrompt systemPrompt = aiClientSystemPromptDao.queryByPromptId(id);
            return ResponseEntity.ok(systemPrompt);
        } catch (Exception e) {
            log.error("查询系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增系统提示词", description = "添加新的系统提示词")
    @RequestMapping(value = "addSystemPrompt", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addSystemPrompt(
            @Parameter(description = "系统提示词信息", required = true) @RequestBody AiClientSystemPrompt aiClientSystemPrompt) {
        try {
            aiClientSystemPrompt.setCreateTime(LocalDateTime.now());
            aiClientSystemPrompt.setUpdateTime(LocalDateTime.now());
            int count = aiClientSystemPromptDao.insert(aiClientSystemPrompt);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新系统提示词", description = "更新现有系统提示词信息")
    @RequestMapping(value = "updateSystemPrompt", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateSystemPrompt(
            @Parameter(description = "系统提示词信息", required = true) @RequestBody AiClientSystemPrompt aiClientSystemPrompt) {
        try {
            aiClientSystemPrompt.setUpdateTime(LocalDateTime.now());
            int count = aiClientSystemPromptDao.updateByPromptId(aiClientSystemPrompt);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除系统提示词", description = "根据ID删除系统提示词")
    @RequestMapping(value = "deleteSystemPrompt", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteSystemPrompt(
            @Parameter(description = "系统提示词ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiClientSystemPromptDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

package com.mingliu.trigger.http.admin;

import com.mingliu.infrastructure.dao.IAiClientSystemPromptDao;
import com.mingliu.infrastructure.dao.po.AiClientSystemPrompt;
import com.mingliu.trigger.http.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import java.util.List;

/**
 * @Title: AiAdminClientSystemPromptController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 23:19
 * @description: 系统提示词管理服务
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/system/prompt/")
@Tag(name = "系统提示词管理", description = "提供系统提示词的增删改查接口")
public class AiAdminClientSystemPromptController {

    @Resource
    private IAiClientSystemPromptDao aiClientSystemPromptDao;

    /**
     * 分页查询系统提示词列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询系统提示词列表", description = "分页查询所有系统提示词信息")
    @PostMapping("querySystemPromptList")
    public ResponseEntity<PageResponse<AiClientSystemPrompt>> querySystemPromptList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClientSystemPrompt> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClientSystemPrompt> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClientSystemPrompt::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClientSystemPrompt::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiClientSystemPrompt::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiClientSystemPrompt::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiClientSystemPrompt> aiClientSystemPromptPage = aiClientSystemPromptDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(aiClientSystemPromptPage));
        } catch (Exception e) {
            log.error("查询系统提示词列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "查询所有系统提示词配置", description = "获取所有系统提示词配置信息")
    @PostMapping("queryAllSystemPromptConfig")
    public ResponseEntity<List<AiClientSystemPrompt>> queryAllSystemPromptConfig() {
        try {
            List<AiClientSystemPrompt> systemPromptList = aiClientSystemPromptDao.selectList(null);
            return ResponseEntity.ok(systemPromptList);
        } catch (Exception e) {
            log.error("查询系统提示词列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询系统提示词
     *
     * @param id 系统提示词ID
     * @return 系统提示词
     */
    @Operation(summary = "根据ID查询系统提示词", description = "获取指定ID的系统提示词详细信息")
    @GetMapping("querySystemPromptById")
    public ResponseEntity<AiClientSystemPrompt> querySystemPromptById(@Parameter(description = "系统提示词ID") @RequestParam("id") Long id) {
        try {
            AiClientSystemPrompt systemPrompt = aiClientSystemPromptDao.selectById(id);
            return ResponseEntity.ok(systemPrompt);
        } catch (Exception e) {
            log.error("查询系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增系统提示词
     *
     * @param aiClientSystemPrompt 系统提示词
     * @return 结果
     */
    @Operation(summary = "新增系统提示词", description = "创建新的系统提示词")
    @PostMapping("addSystemPrompt")
    public ResponseEntity<Boolean> addSystemPrompt(@Parameter(description = "系统提示词信息") @RequestBody AiClientSystemPrompt aiClientSystemPrompt) {
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

    /**
     * 更新系统提示词
     *
     * @param aiClientSystemPrompt 系统提示词
     * @return 结果
     */
    @Operation(summary = "更新系统提示词", description = "更新现有系统提示词的信息")
    @PostMapping("updateSystemPrompt")
    public ResponseEntity<Boolean> updateSystemPrompt(@Parameter(description = "系统提示词信息") @RequestBody AiClientSystemPrompt aiClientSystemPrompt) {
        try {
            aiClientSystemPrompt.setUpdateTime(LocalDateTime.now());
            int count = aiClientSystemPromptDao.updateById(aiClientSystemPrompt);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除系统提示词
     *
     * @param id 系统提示词ID
     * @return 结果
     */
    @Operation(summary = "删除系统提示词", description = "删除指定ID的系统提示词")
    @GetMapping("deleteSystemPrompt")
    public ResponseEntity<Boolean> deleteSystemPrompt(@Parameter(description = "系统提示词ID") @RequestParam("id") Long id) {
        try {
            int count = aiClientSystemPromptDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除系统提示词异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.infrastructure.dao.IAiAgentDao;
import com.mingliu.infrastructure.dao.po.AiAgent;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Title: AiAdminAgentController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 21:42
 * @description: 智能体管理
 */


@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/agent/")
@Tag(name = "AI智能体管理", description = "提供智能体的增删改查接口")
public class AiAdminAgentController {
    @Resource
    private IAiAgentDao aiAgentDao;

    /**
     * 分页查询AI智能体列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询智能体列表", description = "分页查询所有智能体信息")
    @PostMapping("queryAiAgentList")
    public ResponseEntity<PageResponse<AiAgent>> queryAiAgentList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiAgent> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiAgent> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiAgent::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiAgent::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiAgent::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiAgent::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiAgent> aiAgentPage = aiAgentDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(aiAgentPage));
        } catch (Exception e) {
            log.error("查询AI智能体列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据渠道查询智能体列表", description = "获取指定渠道的所有智能体信息")
    @PostMapping("queryAllAgentConfigListByChannel")
    public ResponseEntity<List<AiAgent>> queryAllAgentConfig(@Parameter(description = "渠道标识") @RequestParam("channel") String channel) {
        try {
            List<AiAgent> aiAgentList = aiAgentDao.selectList(new LambdaQueryWrapper<AiAgent>().eq(AiAgent::getChannel, channel));
            return ResponseEntity.ok(aiAgentList);
        } catch (Exception e) {
            log.error("查询AI智能体列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询AI智能体
     *
     * @param id AI智能体ID
     * @return AI智能体
     */
    @Operation(summary = "根据ID查询智能体", description = "获取指定ID的智能体详细信息")
    @GetMapping("queryAiAgentById")
    public ResponseEntity<AiAgent> queryAiAgentById(@Parameter(description = "智能体ID") @RequestParam("id") Long id) {
        try {
            AiAgent aiAgent = aiAgentDao.selectById(id);
            return ResponseEntity.ok(aiAgent);
        } catch (Exception e) {
            log.error("查询AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    @Operation(summary = "新增智能体", description = "创建新的智能体")
    @PostMapping("addAiAgent")
    public ResponseEntity<Boolean> addAiAgent(@Parameter(description = "智能体信息") @RequestBody AiAgent aiAgent) {
        try {
            aiAgent.setCreateTime(LocalDateTime.now());
            aiAgent.setUpdateTime(LocalDateTime.now());
            int count = aiAgentDao.insert(aiAgent);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    @Operation(summary = "更新智能体", description = "更新现有智能体的信息")
    @PostMapping("updateAiAgent")
    public ResponseEntity<Boolean> updateAiAgent(@Parameter(description = "智能体信息") @RequestBody AiAgent aiAgent) {
        try {
            aiAgent.setUpdateTime(LocalDateTime.now());
            int count = aiAgentDao.updateById(aiAgent);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除AI智能体
     *
     * @param id AI智能体ID
     * @return 结果
     */
    @Operation(summary = "删除智能体", description = "删除指定ID的智能体")
    @GetMapping("deleteAiAgent")
    public ResponseEntity<Boolean> deleteAiAgent(@Parameter(description = "智能体ID") @RequestParam("id") Long id) {
        try {
            int count = aiAgentDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

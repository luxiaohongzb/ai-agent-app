package com.mingliu.trigger.http.admin;

import com.mingliu.infrastructure.dao.IAiClientAdvisorDao;
import com.mingliu.infrastructure.dao.po.AiClientAdvisor;
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
 * @Title: AiAdminClientAdvisorController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 22:20
 * @description:
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/advisor/")
@Tag(name = "客户端顾问管理", description = "提供客户端顾问的增删改查接口")
public class AiAdminClientAdvisorController {

    @Resource
    private IAiClientAdvisorDao aiClientAdvisorDao;

    /**
     *
     * @param request
     * @return
     */
    @Operation(summary = "查询客户端顾问列表", description = "分页查询所有客户端顾问信息")
    @PostMapping("queryClientAdvisorList")
    public ResponseEntity<PageResponse<AiClientAdvisor>> queryClientAdvisorList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClientAdvisor> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClientAdvisor> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClientAdvisor::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClientAdvisor::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiClientAdvisor::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiClientAdvisor::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiClientAdvisor> aiClientAdvisorPage = aiClientAdvisorDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(aiClientAdvisorPage));
        } catch (Exception e) {
            log.error("查询客户端顾问列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询客户端顾问
     *
     * @param id 客户端顾问ID
     * @return 客户端顾问
     */
    @Operation(summary = "根据ID查询客户端顾问", description = "获取指定ID的客户端顾问详细信息")
    @GetMapping("queryClientAdvisorById")
    public ResponseEntity<AiClientAdvisor> queryClientAdvisorById(@Parameter(description = "顾问ID") @RequestParam("id") Long id) {
        try {
            AiClientAdvisor aiClientAdvisor = aiClientAdvisorDao.selectById(id);
            return ResponseEntity.ok(aiClientAdvisor);
        } catch (Exception e) {
            log.error("查询客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端顾问
     *
     * @param aiClientAdvisor 客户端顾问
     * @return 结果
     */
    @Operation(summary = "新增客户端顾问", description = "创建新的客户端顾问")
    @PostMapping("addClientAdvisor")
    public ResponseEntity<Boolean> addClientAdvisor(@Parameter(description = "顾问信息") @RequestBody AiClientAdvisor aiClientAdvisor) {
        try {
            aiClientAdvisor.setCreateTime(LocalDateTime.now());
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            int count = aiClientAdvisorDao.insert(aiClientAdvisor);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端顾问
     *
     * @param aiClientAdvisor 客户端顾问
     * @return 结果
     */
    @Operation(summary = "更新客户端顾问", description = "更新现有客户端顾问的信息")
    @PostMapping("updateClientAdvisor")
    public ResponseEntity<Boolean> updateClientAdvisor(@Parameter(description = "顾问信息") @RequestBody AiClientAdvisor aiClientAdvisor) {
        try {
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            int count = aiClientAdvisorDao.updateById(aiClientAdvisor);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除客户端顾问
     *
     * @param id 客户端顾问ID
     * @return 结果
     */
    @Operation(summary = "删除客户端顾问", description = "删除指定ID的客户端顾问")
    @GetMapping("deleteClientAdvisor")
    public ResponseEntity<Boolean> deleteClientAdvisor(@Parameter(description = "顾问ID") @RequestParam("id") Long id) {
        try {
            int count = aiClientAdvisorDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

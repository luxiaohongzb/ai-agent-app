package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import com.mingliu.infrastructure.dao.IAiClientRagOrderDao;
import com.mingliu.infrastructure.dao.po.AiClientRagOrder;
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
 * @Title: AiAdminRagOrderController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 23:49
 * @description: RAG订单管理服务
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/rag/")
@Tag(name = "RAG订单管理", description = "提供RAG订单的增删改查接口")
public class AiAdminRagOrderController {

    @Resource
    private IAiClientRagOrderDao aiRagOrderDao;

    /**
     * 分页查询RAG订单列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询RAG订单列表", description = "分页查询所有RAG订单信息")
    @PostMapping("queryRagOrderList")
    public ResponseEntity<PageResponse<AiClientRagOrder>> queryRagOrderList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClientRagOrder> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClientRagOrder> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClientRagOrder::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClientRagOrder::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiClientRagOrder::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiClientRagOrder::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiClientRagOrder> aiClientRagOrderPage = aiRagOrderDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(aiClientRagOrderPage));
        } catch (Exception e) {
            log.error("查询RAG订单列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "queryAllValidRagOrder", method = RequestMethod.POST)
    public ResponseEntity<List<AiClientRagOrder>> queryAllValidRagOrder() {
        try {
            List<AiClientRagOrder> ragOrderList = aiRagOrderDao.selectList(new LambdaQueryWrapper<AiClientRagOrder>().eq(AiClientRagOrder::getStatus,1));
            return ResponseEntity.ok(ragOrderList);
        } catch (Exception e) {
            log.error("查询RAG订单列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询RAG订单
     *
     * @param id RAG订单ID
     * @return RAG订单
     */
    @Operation(summary = "根据ID查询RAG订单", description = "获取指定ID的RAG订单详细信息")
    @GetMapping("queryRagOrderById")
    public ResponseEntity<AiClientRagOrder> queryRagOrderById(@Parameter(description = "订单ID") @RequestParam("id") Long id) {
        try {
            AiClientRagOrder ragOrder = aiRagOrderDao.selectById(id);
            return ResponseEntity.ok(ragOrder);
        } catch (Exception e) {
            log.error("查询RAG订单异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增RAG订单
     *
     * @param aiRagOrder RAG订单
     * @return 结果
     */
    @Operation(summary = "新增RAG订单", description = "创建新的RAG订单")
    @PostMapping("addRagOrder")
    public ResponseEntity<Boolean> addRagOrder(@Parameter(description = "订单信息") @RequestBody AiClientRagOrder aiRagOrder) {
        try {
            aiRagOrder.setCreateTime(LocalDateTime.now());
            aiRagOrder.setUpdateTime(LocalDateTime.now());
            int count = aiRagOrderDao.insert(aiRagOrder);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增RAG订单异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新RAG订单
     *
     * @param aiRagOrder RAG订单
     * @return 结果
     */
    @Operation(summary = "更新RAG订单", description = "更新现有RAG订单的信息")
    @PostMapping("updateRagOrder")
    public ResponseEntity<Boolean> updateRagOrder(@Parameter(description = "订单信息") @RequestBody AiClientRagOrder aiRagOrder) {
        try {
            aiRagOrder.setUpdateTime(LocalDateTime.now());
            int count = aiRagOrderDao.updateById(aiRagOrder);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新RAG订单异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除RAG订单
     *
     * @param id RAG订单ID
     * @return 结果
     */
    @Operation(summary = "删除RAG订单", description = "删除指定ID的RAG订单")
    @GetMapping("deleteRagOrder")
    public ResponseEntity<Boolean> deleteRagOrder(@Parameter(description = "订单ID") @RequestParam("id") Long id) {
        try {
            int count = aiRagOrderDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除RAG订单异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

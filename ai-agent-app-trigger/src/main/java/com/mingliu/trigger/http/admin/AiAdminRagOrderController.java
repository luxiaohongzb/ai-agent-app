package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiRagOrderDao;
import com.mingliu.infrastructure.dao.po.AiRagOrder;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import com.mingliu.trigger.http.dto.RagOrderQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * RAG顺序管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 16:46
 */
@Tag(name = "RAG顺序管理", description = "RAG顺序相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/rag/")
public class AiAdminRagOrderController {

    @Resource
    private IAiRagOrderDao aiRagOrderDao;

    @Operation(summary = "查询RAG顺序列表", description = "分页查询RAG顺序列表")
    @RequestMapping(value = "queryRagOrderList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiRagOrder>> queryRagOrderList(
            @Parameter(description = "查询条件和分页参数", required = true) @RequestBody RagOrderQueryRequest request) {
        try {
            // 设置分页参数
            if (StringUtils.hasText(request.getOrderBy())) {
                PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            } else {
                PageHelper.startPage(request.getPageNum(), request.getPageSize());
            }
            
            // 执行查询
            AiRagOrder queryCondition = request.toAiRagOrder();
            List<AiRagOrder> ragOrderList = aiRagOrderDao.queryRagOrderList(queryCondition);
            
            // 包装分页结果
            PageInfo<AiRagOrder> pageInfo = new PageInfo<>(ragOrderList);
            PageResponse<AiRagOrder> pageResponse = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            log.error("查询RAG顺序列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "查询所有有效RAG顺序", description = "获取所有有效的RAG顺序列表")
    @RequestMapping(value = "queryAllValidRagOrder", method = RequestMethod.POST)
    public ResponseEntity<List<AiRagOrder>> queryAllValidRagOrder() {
        try {
            List<AiRagOrder> ragOrderList = aiRagOrderDao.queryAllValidRagOrder();
            return ResponseEntity.ok(ragOrderList);
        } catch (Exception e) {
            log.error("查询RAG顺序列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询RAG顺序", description = "根据RAG顺序ID查询详细信息")
    @RequestMapping(value = "queryRagOrderById", method = RequestMethod.GET)
    public ResponseEntity<AiRagOrder> queryRagOrderById(
            @Parameter(description = "RAG顺序ID", required = true) @RequestParam("id") Long id) {
        try {
            AiRagOrder ragOrder = aiRagOrderDao.queryRagOrderById(id);
            return ResponseEntity.ok(ragOrder);
        } catch (Exception e) {
            log.error("查询RAG顺序异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增RAG顺序", description = "添加新的RAG顺序")
    @RequestMapping(value = "addRagOrder", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addRagOrder(
            @Parameter(description = "RAG顺序信息", required = true) @RequestBody AiRagOrder aiRagOrder) {
        try {
            aiRagOrder.setCreateTime(new Date());
            aiRagOrder.setUpdateTime(new Date());
            int count = aiRagOrderDao.insert(aiRagOrder);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增RAG顺序异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新RAG顺序", description = "更新现有RAG顺序信息")
    @RequestMapping(value = "updateRagOrder", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateRagOrder(
            @Parameter(description = "RAG顺序信息", required = true) @RequestBody AiRagOrder aiRagOrder) {
        try {
            aiRagOrder.setUpdateTime(new Date());
            int count = aiRagOrderDao.update(aiRagOrder);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新RAG顺序异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除RAG顺序", description = "根据ID删除RAG顺序")
    @RequestMapping(value = "deleteRagOrder", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteRagOrder(
            @Parameter(description = "RAG顺序ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiRagOrderDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除RAG顺序异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.infrastructure.dao.IAiClientToolMcpDao;
import com.mingliu.infrastructure.dao.po.AiClientToolMcp;
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
 * @Title: AiAdminClientToolMcpController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 23:47
 * @description: MCP工具管理服务
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/tool/mcp/")
@Tag(name = "MCP工具管理", description = "提供MCP工具配置的增删改查接口")
public class AiAdminClientToolMcpController {
    @Resource
    private IAiClientToolMcpDao aiClientToolMcpDao;

    /**
     * 分页查询MCP配置列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询MCP配置列表", description = "分页查询所有MCP工具配置信息")
    @PostMapping("queryMcpList")
    public ResponseEntity<PageResponse<AiClientToolMcp>> queryMcpList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClientToolMcp> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClientToolMcp> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClientToolMcp::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClientToolMcp::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiClientToolMcp::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiClientToolMcp::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiClientToolMcp> mcpPage = aiClientToolMcpDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(mcpPage));
        } catch (Exception e) {
            log.error("查询MCP配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询MCP配置
     *
     * @param id MCP配置ID
     * @return MCP配置
     */
    @Operation(summary = "根据ID查询MCP配置", description = "获取指定ID的MCP工具配置详细信息")
    @GetMapping("queryMcpById")
    public ResponseEntity<AiClientToolMcp> queryMcpById(@Parameter(description = "MCP配置ID") @RequestParam("id") Long id) {
        try {
            AiClientToolMcp mcp = aiClientToolMcpDao.selectById(id);
            return ResponseEntity.ok(mcp);
        } catch (Exception e) {
            log.error("查询MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增MCP配置
     *
     * @param aiClientToolMcp MCP配置
     * @return 结果
     */
    @Operation(summary = "新增MCP配置", description = "创建新的MCP工具配置")
    @PostMapping("addMcp")
    public ResponseEntity<Boolean> addMcp(@Parameter(description = "MCP配置信息") @RequestBody AiClientToolMcp aiClientToolMcp) {
        try {
            aiClientToolMcp.setCreateTime(LocalDateTime.now());
            aiClientToolMcp.setUpdateTime(LocalDateTime.now());
            int count = aiClientToolMcpDao.insert(aiClientToolMcp);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新MCP配置
     *
     * @param aiClientToolMcp MCP配置
     * @return 结果
     */
    @Operation(summary = "更新MCP配置", description = "更新现有MCP工具配置的信息")
    @PostMapping("updateMcp")
    public ResponseEntity<Boolean> updateMcp(@Parameter(description = "MCP配置信息") @RequestBody AiClientToolMcp aiClientToolMcp) {
        try {
            aiClientToolMcp.setUpdateTime(LocalDateTime.now());
            int count = aiClientToolMcpDao.updateById(aiClientToolMcp);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除MCP配置
     *
     * @param id MCP配置ID
     * @return 结果
     */
    @Operation(summary = "删除MCP配置", description = "删除指定ID的MCP工具配置")
    @GetMapping("deleteMcp")
    public ResponseEntity<Boolean> deleteMcp(@Parameter(description = "MCP配置ID") @RequestParam("id") Long id) {
        try {
            int count = aiClientToolMcpDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

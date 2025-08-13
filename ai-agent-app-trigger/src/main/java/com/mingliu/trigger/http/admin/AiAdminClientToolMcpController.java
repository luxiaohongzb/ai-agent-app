package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MCP工具管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 16:01
 */
@Tag(name = "MCP工具管理", description = "MCP工具相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/tool/mcp/")
public class AiAdminClientToolMcpController {

    @Resource
    private IAiClientToolMcpDao aiClientToolMcpDao;

    @Operation(summary = "查询MCP配置列表", description = "获取所有MCP配置列表")
    @RequestMapping(value = "queryMcpList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientToolMcp>> queryMcpList(
            @Parameter(description = "查询条件", required = true) @RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize(),  request.getOrderBy());
            
            List<AiClientToolMcp> mcpList = aiClientToolMcpDao.queryAll();
            
            // 包装分页结果
            PageInfo<AiClientToolMcp> pageInfo = new PageInfo<>(mcpList);
            PageResponse<AiClientToolMcp> response = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询MCP配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询MCP配置", description = "根据MCP配置ID查询详细信息")
    @RequestMapping(value = "queryMcpById", method = RequestMethod.GET)
    public ResponseEntity<AiClientToolMcp> queryMcpById(
            @Parameter(description = "MCP配置ID", required = true) @RequestParam("id") Long id) {
        try {
            AiClientToolMcp mcp = aiClientToolMcpDao.queryById(id);
            return ResponseEntity.ok(mcp);
        } catch (Exception e) {
            log.error("查询MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增MCP配置", description = "添加新的MCP配置")
    @RequestMapping(value = "addMcp", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addMcp(
            @Parameter(description = "MCP配置信息", required = true) @RequestBody AiClientToolMcp aiClientToolMcp) {
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

    @Operation(summary = "更新MCP配置", description = "更新现有MCP配置信息")
    @RequestMapping(value = "updateMcp", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateMcp(
            @Parameter(description = "MCP配置信息", required = true) @RequestBody AiClientToolMcp aiClientToolMcp) {
        try {
            aiClientToolMcp.setUpdateTime(LocalDateTime.now());
            int count = aiClientToolMcpDao.updateByMcpId(aiClientToolMcp);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除MCP配置", description = "根据ID删除MCP配置")
    @RequestMapping(value = "deleteMcp", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteMcp(
            @Parameter(description = "MCP配置ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiClientToolMcpDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除MCP配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

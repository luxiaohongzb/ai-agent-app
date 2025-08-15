package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientConfigDao;
import com.mingliu.infrastructure.dao.po.AiClientConfig;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import com.mingliu.trigger.http.vo.AiClientToolConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: AiAdminClientToolConfigController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 23:33
 * @description: 客户端工具配置列表
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/tool/config/")
@Tag(name = "客户端工具配置管理", description = "提供客户端工具配置的增删改查接口")
public class AiAdminClientToolConfigController {
    @Resource
    private IAiClientConfigDao aiClientConfigDao;

    /**
     * 分页查询客户端工具配置列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询客户端工具配置列表", description = "分页查询所有客户端工具配置信息")
    @PostMapping("queryClientToolConfigList")
    public ResponseEntity<PageResponse<AiClientToolConfig>> queryClientToolConfigList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<AiClientConfig> wrapper = new LambdaQueryWrapper<AiClientConfig>()
                    .eq(AiClientConfig::getSourceType, "client")
                    .eq(AiClientConfig::getTargetType, "tool_mcp")
                    .eq(request.getId() != null, AiClientConfig::getId, request.getId())
                    .eq(request.getStatus() != null, AiClientConfig::getStatus, request.getStatus())
                    .ge(request.getCreateTimeStart() != null, AiClientConfig::getCreateTime, request.getCreateTimeStart())
                    .le(request.getCreateTimeEnd() != null, AiClientConfig::getCreateTime, request.getCreateTimeEnd());

            // 执行分页查询
            Page<AiClientConfig> page = new Page<>(request.getPageNum(), request.getPageSize());
            Page<AiClientConfig> configPage = aiClientConfigDao.selectPage(page, wrapper);

            // 转换为VO
            List<AiClientToolConfig> resultList = new ArrayList<>();
            for (AiClientConfig config : configPage.getRecords()) {
                AiClientToolConfig vo = new AiClientToolConfig();
                vo.setId(config.getId());
                vo.setClientId(Long.valueOf(config.getSourceId()));
                vo.setToolType("tool_mcp");
                vo.setToolId(Long.valueOf(config.getTargetId()));
                vo.setCreateTime(config.getCreateTime());
                resultList.add(vo);
            }

            // 构建分页响应
            PageResponse<AiClientToolConfig> response = new PageResponse<>();
            response.setPageNum((int) configPage.getCurrent());
            response.setPageSize((int) configPage.getSize());
            response.setTotal(configPage.getTotal());
            response.setPages((int) configPage.getPages());
            response.setList(resultList);
            response.setHasNextPage(configPage.hasNext());
            response.setHasPreviousPage(configPage.hasPrevious());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询客户端工具配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询客户端工具配置
     *
     * @param id 客户端工具配置ID
     * @return 客户端工具配置
     */
    @Operation(summary = "根据ID查询客户端工具配置", description = "获取指定ID的客户端工具配置详细信息")
    @GetMapping("queryClientToolConfigById")
    public ResponseEntity<AiClientToolConfig> queryClientToolConfigById(@Parameter(description = "配置ID") @RequestParam("id") Long id) {
        try {
            AiClientConfig config = aiClientConfigDao.selectById(id);
            if (config == null || !"tool_mcp".equals(config.getTargetType())) {
                return ResponseEntity.notFound().build();
            }

            // 转换为VO
            AiClientToolConfig vo = new AiClientToolConfig();
            vo.setId(config.getId());
            vo.setClientId(Long.valueOf(config.getSourceId()));
            vo.setToolType("tool_mcp");
            vo.setToolId(Long.valueOf(config.getTargetId()));
            vo.setCreateTime(config.getCreateTime());

            return ResponseEntity.ok(vo);
        } catch (Exception e) {
            log.error("查询客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据客户端ID查询工具配置列表
     *
     * @param clientId 客户端ID
     * @return 工具配置列表
     */
    @Operation(summary = "根据客户端ID查询工具配置列表", description = "获取指定客户端的所有工具配置信息")
    @GetMapping("queryClientToolConfigByClientId")
    public ResponseEntity<List<AiClientToolConfig>> queryClientToolConfigByClientId(@Parameter(description = "客户端ID") @RequestParam("clientId") Long clientId) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<AiClientConfig> wrapper = new LambdaQueryWrapper<AiClientConfig>()
                    .eq(AiClientConfig::getSourceType, "client")
                    .eq(AiClientConfig::getSourceId, String.valueOf(clientId))
                    .eq(AiClientConfig::getTargetType, "tool_mcp")
                  ;

            // 执行查询
            List<AiClientConfig> configList = aiClientConfigDao.selectList(wrapper);

            // 转换为VO
            List<AiClientToolConfig> resultList = new ArrayList<>();
            for (AiClientConfig config : configList) {
                AiClientToolConfig vo = new AiClientToolConfig();
                vo.setId(config.getId());
                vo.setClientId(Long.valueOf(config.getSourceId()));
                vo.setToolType("tool_mcp");
                vo.setToolId(Long.valueOf(config.getTargetId()));
                vo.setCreateTime(config.getCreateTime());
                resultList.add(vo);
            }

            return ResponseEntity.ok(resultList);
        } catch (Exception e) {
            log.error("根据客户端ID查询工具配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端工具配置
     *
     * @param aiClientToolConfig 客户端工具配置
     * @return 结果
     */
    @Operation(summary = "新增客户端工具配置", description = "创建新的客户端工具配置")
    @PostMapping("addClientToolConfig")
    public ResponseEntity<Boolean> addClientToolConfig(@Parameter(description = "工具配置信息") @RequestBody AiClientToolConfig aiClientToolConfig) {
        try {
            // 转换为PO
            AiClientConfig config = new AiClientConfig();
            config.setSourceType("client");
            config.setSourceId(String.valueOf(aiClientToolConfig.getClientId()));
            config.setTargetType("tool_mcp");
            config.setTargetId(String.valueOf(aiClientToolConfig.getToolId()));
            config.setStatus(0);
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());

            int count = aiClientConfigDao.insert(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端工具配置
     *
     * @param aiClientToolConfig 客户端工具配置
     * @return 结果
     */
    @Operation(summary = "更新客户端工具配置", description = "更新现有客户端工具配置的信息")
    @PostMapping("updateClientToolConfig")
    public ResponseEntity<Boolean> updateClientToolConfig(@Parameter(description = "工具配置信息") @RequestBody AiClientToolConfig aiClientToolConfig) {
        try {
            // 转换为PO
            AiClientConfig config = new AiClientConfig();
            config.setId(aiClientToolConfig.getId());
            config.setSourceType("client");
            config.setSourceId(String.valueOf(aiClientToolConfig.getClientId()));
            config.setTargetType("tool_mcp");
            config.setTargetId(String.valueOf(aiClientToolConfig.getToolId()));
            config.setUpdateTime(LocalDateTime.now());

            int count = aiClientConfigDao.updateById(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除客户端工具配置
     *
     * @param id 客户端工具配置ID
     * @return 结果
     */
    @Operation(summary = "删除客户端工具配置", description = "删除指定ID的客户端工具配置（逻辑删除）")
    @GetMapping("deleteClientToolConfig")
    public ResponseEntity<Boolean> deleteClientToolConfig(@Parameter(description = "配置ID") @RequestParam("id") Long id) {
        try {
            // 逻辑删除，将状态设置为0
            AiClientConfig config = new AiClientConfig();
            config.setId(id);
            config.setStatus(0);
            config.setUpdateTime(LocalDateTime.now());

            int count = aiClientConfigDao.updateById(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

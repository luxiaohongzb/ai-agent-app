package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientToolConfigDao;
import com.mingliu.infrastructure.dao.po.AiClientToolConfig;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.ClientToolConfigQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
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
 * 客户端工具配置管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 16:44
 */
@Tag(name = "客户端工具配置管理", description = "客户端工具配置相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/tool/config/")
public class AiAdminClientToolConfigController {

    @Resource
    private IAiClientToolConfigDao aiClientToolConfigDao;

    @Operation(summary = "查询客户端工具配置列表", description = "分页查询客户端工具配置列表")
    @RequestMapping(value = "queryClientToolConfigList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientToolConfig>> queryClientToolConfigList(
            @Parameter(description = "查询条件和分页参数", required = true) @RequestBody ClientToolConfigQueryRequest request) {
        try {
            // 设置分页参数
            if (StringUtils.hasText(request.getOrderBy())) {
                PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            } else {
                PageHelper.startPage(request.getPageNum(), request.getPageSize());
            }
            
            // 执行查询
            AiClientToolConfig queryCondition = request.toAiClientToolConfig();
            List<AiClientToolConfig> configList = aiClientToolConfigDao.queryToolConfigList(queryCondition);
            
            // 包装分页结果
            PageInfo<AiClientToolConfig> pageInfo = new PageInfo<>(configList);
            PageResponse<AiClientToolConfig> pageResponse = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            log.error("查询客户端工具配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询客户端工具配置", description = "根据配置ID查询客户端工具配置详细信息")
    @RequestMapping(value = "queryClientToolConfigById", method = RequestMethod.GET)
    public ResponseEntity<AiClientToolConfig> queryClientToolConfigById(
            @Parameter(description = "客户端工具配置ID", required = true) @RequestParam("id") Long id) {
        try {
            AiClientToolConfig config = aiClientToolConfigDao.queryToolConfigById(id);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            log.error("查询客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据客户端ID查询工具配置列表", description = "根据客户端ID查询对应的工具配置列表")
    @RequestMapping(value = "queryClientToolConfigByClientId", method = RequestMethod.GET)
    public ResponseEntity<List<AiClientToolConfig>> queryClientToolConfigByClientId(
            @Parameter(description = "客户端ID", required = true) @RequestParam("clientId") Long clientId) {
        try {
            List<AiClientToolConfig> configList = aiClientToolConfigDao.queryToolConfigByClientId(clientId);
            return ResponseEntity.ok(configList);
        } catch (Exception e) {
            log.error("根据客户端ID查询工具配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增客户端工具配置", description = "添加新的客户端工具配置")
    @RequestMapping(value = "addClientToolConfig", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addClientToolConfig(
            @Parameter(description = "客户端工具配置信息", required = true) @RequestBody AiClientToolConfig aiClientToolConfig) {
        try {
            aiClientToolConfig.setCreateTime(new Date());
            int count = aiClientToolConfigDao.insert(aiClientToolConfig);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新客户端工具配置", description = "更新现有客户端工具配置信息")
    @RequestMapping(value = "updateClientToolConfig", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateClientToolConfig(
            @Parameter(description = "客户端工具配置信息", required = true) @RequestBody AiClientToolConfig aiClientToolConfig) {
        try {
            int count = aiClientToolConfigDao.update(aiClientToolConfig);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除客户端工具配置", description = "根据ID删除客户端工具配置")
    @RequestMapping(value = "deleteClientToolConfig", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteClientToolConfig(
            @Parameter(description = "客户端工具配置ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiClientToolConfigDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端工具配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

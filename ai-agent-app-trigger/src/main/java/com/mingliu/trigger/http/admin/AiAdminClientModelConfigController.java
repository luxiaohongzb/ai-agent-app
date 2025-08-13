package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientModelConfigDao;
import com.mingliu.infrastructure.dao.po.AiClientModelConfig;
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

import java.util.Date;
import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 16:43
 */
@Tag(name = "客户端模型配置管理", description = "客户端模型配置相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/model/config/")
public class AiAdminClientModelConfigController {

    @Resource
    private IAiClientModelConfigDao aiClientModelConfigDao;

    @Operation(summary = "查询客户端模型配置列表", description = "根据条件查询客户端模型配置列表")
    @RequestMapping(value = "queryClientModelConfigList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientModelConfig>> queryClientModelConfigList(
            @Parameter(description = "查询条件", required = true) @RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize(),  request.getOrderBy());
            
            List<AiClientModelConfig> configList = aiClientModelConfigDao.queryAllModelConfig();
            
            // 包装分页结果
            PageInfo<AiClientModelConfig> pageInfo = new PageInfo<>(configList);
            PageResponse<AiClientModelConfig> response = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询客户端模型配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询客户端模型配置", description = "根据配置ID查询客户端模型配置详细信息")
    @RequestMapping(value = "queryClientModelConfigById", method = RequestMethod.GET)
    public ResponseEntity<AiClientModelConfig> queryClientModelConfigById(
            @Parameter(description = "客户端模型配置ID", required = true) @RequestParam("id") Long id) {
        try {
            AiClientModelConfig config = aiClientModelConfigDao.queryModelConfigById(id);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            log.error("查询客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据客户端ID查询模型配置", description = "根据客户端ID查询对应的模型配置")
    @RequestMapping(value = "queryClientModelConfigByClientId", method = RequestMethod.GET)
    public ResponseEntity<AiClientModelConfig> queryClientModelConfigByClientId(
            @Parameter(description = "客户端ID", required = true) @RequestParam("clientId") Long clientId) {
        try {
            AiClientModelConfig config = aiClientModelConfigDao.queryModelConfigByClientId(clientId);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            log.error("根据客户端ID查询模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据模型ID查询客户端模型配置列表", description = "根据模型ID查询对应的客户端模型配置列表")
    @RequestMapping(value = "queryClientModelConfigByModelId", method = RequestMethod.GET)
    public ResponseEntity<List<AiClientModelConfig>> queryClientModelConfigByModelId(
            @Parameter(description = "模型ID", required = true) @RequestParam("modelId") Long modelId) {
        try {
            List<AiClientModelConfig> configList = aiClientModelConfigDao.queryModelConfigByModelId(modelId);
            return ResponseEntity.ok(configList);
        } catch (Exception e) {
            log.error("根据模型ID查询客户端模型配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增客户端模型配置", description = "添加新的客户端模型配置")
    @RequestMapping(value = "addClientModelConfig", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addClientModelConfig(
            @Parameter(description = "客户端模型配置信息", required = true) @RequestBody AiClientModelConfig aiClientModelConfig) {
        try {
            aiClientModelConfig.setCreateTime(new Date());
            int count = aiClientModelConfigDao.insert(aiClientModelConfig);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新客户端模型配置", description = "更新现有客户端模型配置信息")
    @RequestMapping(value = "updateClientModelConfig", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateClientModelConfig(
            @Parameter(description = "客户端模型配置信息", required = true) @RequestBody AiClientModelConfig aiClientModelConfig) {
        try {
            int count = aiClientModelConfigDao.update(aiClientModelConfig);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除客户端模型配置", description = "根据ID删除客户端模型配置")
    @RequestMapping(value = "deleteClientModelConfig", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteClientModelConfig(
            @Parameter(description = "客户端模型配置ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiClientModelConfigDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

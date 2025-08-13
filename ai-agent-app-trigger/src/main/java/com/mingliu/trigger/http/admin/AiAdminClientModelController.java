package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientModelConfigDao;
import com.mingliu.infrastructure.dao.IAiClientModelDao;
import com.mingliu.infrastructure.dao.po.AiClientModel;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 客户端模型管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 15:16
 */
@Tag(name = "客户端模型管理", description = "客户端模型相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/model/")
public class AiAdminClientModelController {

    @Resource
    private IAiClientModelDao aiClientModelDao;

    @Resource
    private IAiClientModelConfigDao aiClientModelConfigDao;

    @Operation(summary = "查询客户端模型列表", description = "获取所有客户端模型列表")
    @RequestMapping(value = "queryClientModelList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientModel>> queryClientModelList(
            @Parameter(description = "查询条件", required = true) @RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            
            List<AiClientModel> aiClientModelList = aiClientModelDao.queryAll();
            
            // 包装分页结果
            PageInfo<AiClientModel> pageInfo = new PageInfo<>(aiClientModelList);
            PageResponse<AiClientModel> response = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询客户端模型列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "查询所有模型配置", description = "获取所有模型配置信息")
    @RequestMapping(value = "queryAllModelConfig", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientModelConfig>> queryAllModelConfig(
            @Parameter(description = "查询条件", required = true) @RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            
            List<AiClientModelConfig> aiClientModelConfigs = aiClientModelConfigDao.queryAllModelConfig();
            
            // 包装分页结果
            PageInfo<AiClientModelConfig> pageInfo = new PageInfo<>(aiClientModelConfigs);
            PageResponse<AiClientModelConfig> response = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询客户端模型列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询客户端模型", description = "根据模型ID查询客户端模型详细信息")
    @RequestMapping(value = "queryClientModelById", method = RequestMethod.GET)
    public ResponseEntity<AiClientModelConfig> queryClientModelById(
            @Parameter(description = "客户端模型ID", required = true) @RequestParam("id") Long id) {
        try {
            AiClientModelConfig aiClientModelConfig = aiClientModelConfigDao.queryModelConfigById(id);
            return ResponseEntity.ok(aiClientModelConfig);
        } catch (Exception e) {
            log.error("查询客户端模型异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增客户端模型", description = "添加新的客户端模型")
    @RequestMapping(value = "addClientModel", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addClientModel(
            @Parameter(description = "客户端模型信息", required = true) @RequestBody AiClientModel aiClientModel) {
        try {
            aiClientModel.setCreateTime(LocalDateTime.now());
            aiClientModel.setUpdateTime(LocalDateTime.now());
            int count = aiClientModelDao.insert(aiClientModel);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端模型异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新客户端模型", description = "更新现有客户端模型信息")
    @RequestMapping(value = "updateClientModel", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateClientModel(
            @Parameter(description = "客户端模型信息", required = true) @RequestBody AiClientModel aiClientModel) {
        try {
            aiClientModel.setUpdateTime(LocalDateTime.now());
            int count = aiClientModelDao.updateByModelId(aiClientModel);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端模型异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除客户端模型", description = "根据ID删除客户端模型")
    @RequestMapping(value = "deleteClientModel", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteClientModel(
            @Parameter(description = "客户端模型ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiClientModelDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端模型异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

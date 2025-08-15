package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.infrastructure.dao.IAiClientConfigDao;
import com.mingliu.infrastructure.dao.IAiClientModelDao;
import com.mingliu.infrastructure.dao.po.AiClientConfig;
import com.mingliu.infrastructure.dao.po.AiClientModel;
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
import java.util.stream.Collectors;

import static io.restassured.path.json.JsonPath.config;

/**
 * @Title: AiAdminModelController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 22:54
 * @description: 模型controller
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/model/")
@Tag(name = "AI模型管理", description = "提供客户端模型配置的增删改查接口")
public class AiAdminModelController {

    @Resource
    private IAiClientModelDao aiClientModelDao;

    @Resource
    private IAiClientConfigDao aiClientConfigDao;

    /**
     * 分页查询客户端模型配置列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询客户端模型配置列表", description = "分页查询所有客户端模型配置信息")
    @PostMapping("queryClientModelList")
    public ResponseEntity<PageResponse<AiClientModel>> queryClientModelConfigList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClientModel> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClientModel> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClientModel::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClientModel::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiClientModel::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiClientModel::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiClientModel> modelPage = aiClientModelDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(modelPage));
        } catch (Exception e) {
            log.error("查询客户端模型配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询客户端模型配置
     *
     * @param id 客户端模型配置ID
     * @return 客户端模型配置
     */
    @Operation(summary = "根据ID查询客户端模型配置", description = "获取指定ID的客户端模型配置详细信息")
    @GetMapping("queryClientModelById")
    public ResponseEntity<AiClientModel> queryClientModelConfigById(@Parameter(description = "配置ID") @RequestParam("id") Long id) {
        try {
            AiClientModel config = aiClientModelDao.selectById(id);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            log.error("查询客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据客户端ID查询模型配置
     *
     * @param clientId 客户端ID
     * @return 模型配置
     */
    @Operation(summary = "根据客户端ID查询模型配置", description = "获取指定客户端的模型配置信息")
    @GetMapping("queryClientModelByClientId")
    public ResponseEntity<AiClientModel> queryClientModelConfigByClientId(@Parameter(description = "客户端ID") @RequestParam("clientId") String clientId) {
        try {
            AiClientConfig aiClientConfig = aiClientConfigDao.selectOne(new LambdaQueryWrapper<AiClientConfig>().eq(AiClientConfig::getSourceType,"client").eq(AiClientConfig::getSourceId,clientId));
            AiClientModel aiClientModels = aiClientModelDao.selectById(aiClientConfig.getId());
            return ResponseEntity.ok(aiClientModels);
        } catch (Exception e) {
            log.error("根据客户端ID查询模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据模型ID查询客户端模型配置列表
     *
     * @param modelId 模型ID
     * @return 客户端模型配置列表
     */
    @Operation(summary = "根据模型ID查询客户端模型配置列表", description = "获取使用指定模型的所有客户端配置信息")
    @GetMapping("queryClientModelByModelId")
    public ResponseEntity<List<AiClientModel>> queryClientModelConfigByModelId(@Parameter(description = "模型ID") @RequestParam("modelId") String modelId) {
        try {
            List<AiClientConfig> configList = aiClientConfigDao.queryBySourceTypeAndId("model",modelId);
            List<Long> list = configList.stream().map(AiClientConfig::getId).toList();
            List<AiClientModel> aiClientModels = aiClientModelDao.selectBatchIds(list);
            return ResponseEntity.ok(aiClientModels);
        } catch (Exception e) {
            log.error("根据模型ID查询客户端模型配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端模型配置
     *
     * @param AiClientModel 客户端模型配置
     * @return 结果
     */
    @Operation(summary = "新增客户端模型配置", description = "创建新的客户端模型配置")
    @PostMapping("addClientModel")
    public ResponseEntity<Boolean> addClientModelConfig(@Parameter(description = "模型配置信息") @RequestBody AiClientModel AiClientModel) {
        try {
            AiClientModel.setCreateTime(LocalDateTime.now());
            int count = aiClientModelDao.insert(AiClientModel);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端模型配置
     *
     * @param AiClientModel 客户端模型配置
     * @return 结果
     */
    @Operation(summary = "更新客户端模型配置", description = "更新现有的客户端模型配置信息")
    @PostMapping("updateClientModel")
    public ResponseEntity<Boolean> updateClientModelConfig(@Parameter(description = "模型配置信息") @RequestBody AiClientModel AiClientModel) {
        try {
            int count = aiClientModelDao.updateById(AiClientModel);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除客户端模型配置
     *
     * @param id 客户端模型配置ID
     * @return 结果
     */
    @Operation(summary = "删除客户端模型配置", description = "删除指定ID的客户端模型配置")
    @GetMapping("deleteClientModelConfig")
    public ResponseEntity<Boolean> deleteClientModelConfig(@Parameter(description = "配置ID") @RequestParam("id") Long id) {
        try {
            int count = aiClientModelDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端模型配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

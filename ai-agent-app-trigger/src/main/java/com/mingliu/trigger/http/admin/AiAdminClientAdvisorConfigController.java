package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.infrastructure.dao.IAiClientConfigDao;
import com.mingliu.infrastructure.dao.po.AiClientConfig;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import com.mingliu.trigger.http.vo.AiClientAdvisorConfig;
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
 * @Title: AiAdminClientAdvisorConfigController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 23:56
 * @description: 顾问-客户端映射
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/advisor/config/")
@Tag(name = "客户端顾问配置管理", description = "提供客户端顾问配置的增删改查接口")
public class AiAdminClientAdvisorConfigController {

    @Resource
    private IAiClientConfigDao aiClientConfigDao;

    /**
     * 分页查询客户端顾问配置列表
     *
     * @param aiClientAdvisorConfig 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询客户端顾问配置列表", description = "分页查询所有客户端顾问配置信息")
    @PostMapping("queryClientAdvisorConfigList")
    public ResponseEntity<PageResponse<AiClientAdvisorConfig>> queryClientAdvisorConfigList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<AiClientConfig> wrapper = new LambdaQueryWrapper<AiClientConfig>()
                    .eq(AiClientConfig::getSourceType, "client")
                    .eq(AiClientConfig::getTargetType, "advisor")
                    .eq(request.getId() != null, AiClientConfig::getId, request.getId())
                    .eq(request.getStatus() != null, AiClientConfig::getStatus, request.getStatus())
                    .ge(request.getCreateTimeStart() != null, AiClientConfig::getCreateTime, request.getCreateTimeStart())
                    .le(request.getCreateTimeEnd() != null, AiClientConfig::getCreateTime, request.getCreateTimeEnd());

            // 执行分页查询
            Page<AiClientConfig> page = new Page<>(request.getPageNum(), request.getPageSize());
            Page<AiClientConfig> configPage = aiClientConfigDao.selectPage(page, wrapper);

            // 转换为VO
            List<AiClientAdvisorConfig> resultList = new ArrayList<>();
            for (AiClientConfig config : configPage.getRecords()) {
                AiClientAdvisorConfig vo = new AiClientAdvisorConfig();
                vo.setId(config.getId());
                vo.setClientId(Long.valueOf(config.getSourceId()));
                vo.setAdvisorId(Long.valueOf(config.getTargetId()));
                vo.setCreateTime(config.getCreateTime());
                resultList.add(vo);
            }

            // 构建分页响应
            PageResponse<AiClientAdvisorConfig> response = new PageResponse<>();
            response.setPageNum((int) configPage.getCurrent());
            response.setPageSize((int) configPage.getSize());
            response.setTotal(configPage.getTotal());
            response.setPages((int) configPage.getPages());
            response.setList(resultList);
            response.setHasNextPage(configPage.hasNext());
            response.setHasPreviousPage(configPage.hasPrevious());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询客户端顾问配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询客户端顾问配置
     *
     * @param id 客户端顾问配置ID
     * @return 客户端顾问配置
     */
    @Operation(summary = "根据ID查询客户端顾问配置", description = "获取指定ID的客户端顾问配置详细信息")
    @GetMapping("queryClientAdvisorConfigById")
    public ResponseEntity<AiClientAdvisorConfig> queryClientAdvisorConfigById(@Parameter(description = "配置ID") @RequestParam Long id) {
        try {
            AiClientConfig config = aiClientConfigDao.selectById(id);
            if (config == null || !"advisor".equals(config.getTargetType())) {
                return ResponseEntity.notFound().build();
            }

            // 转换为VO
            AiClientAdvisorConfig vo = new AiClientAdvisorConfig();
            vo.setId(config.getId());
            vo.setClientId(Long.valueOf(config.getSourceId()));
            vo.setAdvisorId(Long.valueOf(config.getTargetId()));
            vo.setCreateTime(config.getCreateTime());

            return ResponseEntity.ok(vo);
        } catch (Exception e) {
            log.error("查询客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据客户端ID查询顾问配置列表
     *
     * @param clientId 客户端ID
     * @return 顾问配置列表
     */
    @Operation(summary = "根据客户端ID查询顾问配置列表", description = "获取指定客户端的所有顾问配置信息")
    @GetMapping("queryClientAdvisorConfigByClientId")
    public ResponseEntity<List<AiClientAdvisorConfig>> queryClientAdvisorConfigByClientId(@Parameter(description = "客户端ID") @RequestParam("clientId") Long clientId) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<AiClientConfig> wrapper = new LambdaQueryWrapper<AiClientConfig>()
                    .eq(AiClientConfig::getSourceType, "client")
                    .eq(AiClientConfig::getSourceId, String.valueOf(clientId))
                    .eq(AiClientConfig::getTargetType, "advisor")
                    .eq(AiClientConfig::getStatus, 1);

            // 执行查询
            List<AiClientConfig> configList = aiClientConfigDao.selectList(wrapper);

            // 转换为VO
            List<AiClientAdvisorConfig> resultList = new ArrayList<>();
            for (AiClientConfig config : configList) {
                AiClientAdvisorConfig vo = new AiClientAdvisorConfig();
                vo.setId(config.getId());
                vo.setClientId(Long.valueOf(config.getSourceId()));
                vo.setAdvisorId(Long.valueOf(config.getTargetId()));
                vo.setCreateTime(config.getCreateTime());
                resultList.add(vo);
            }

            return ResponseEntity.ok(resultList);
        } catch (Exception e) {
            log.error("根据客户端ID查询顾问配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端顾问配置
     *
     * @param aiClientAdvisorConfig 客户端顾问配置
     * @return 结果
     */
    @Operation(summary = "新增客户端顾问配置", description = "创建新的客户端顾问配置")
    @PostMapping("addClientAdvisorConfig")
    public ResponseEntity<Boolean> addClientAdvisorConfig(@Parameter(description = "顾问配置信息") @RequestBody AiClientAdvisorConfig aiClientAdvisorConfig) {
        try {
            // 转换为PO
            AiClientConfig config = new AiClientConfig();
            config.setSourceType("client");
            config.setSourceId(String.valueOf(aiClientAdvisorConfig.getClientId()));
            config.setTargetType("advisor");
            config.setTargetId(String.valueOf(aiClientAdvisorConfig.getAdvisorId()));
            config.setStatus(1);
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());

            int count = aiClientConfigDao.insert(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端顾问配置
     *
     * @param aiClientAdvisorConfig 客户端顾问配置
     * @return 结果
     */
    @Operation(summary = "更新客户端顾问配置", description = "更新现有客户端顾问配置的信息")
    @PostMapping("updateClientAdvisorConfig")
    public ResponseEntity<Boolean> updateClientAdvisorConfig(@Parameter(description = "顾问配置信息") @RequestBody AiClientAdvisorConfig aiClientAdvisorConfig) {
        try {
            // 转换为PO
            AiClientConfig config = new AiClientConfig();
            config.setId(aiClientAdvisorConfig.getId());
            config.setSourceType("client");
            config.setSourceId(String.valueOf(aiClientAdvisorConfig.getClientId()));
            config.setTargetType("advisor");
            config.setTargetId(String.valueOf(aiClientAdvisorConfig.getAdvisorId()));
            config.setUpdateTime(LocalDateTime.now());

            int count = aiClientConfigDao.updateById(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除客户端顾问配置
     *
     * @param id 客户端顾问配置ID
     * @return 结果
     */
    @Operation(summary = "删除客户端顾问配置", description = "删除指定ID的客户端顾问配置（逻辑删除）")
    @GetMapping("deleteClientAdvisorConfig")
    public ResponseEntity<Boolean> deleteClientAdvisorConfig(@Parameter(description = "配置ID") @RequestParam("id") Long id) {
        try {
            // 逻辑删除，将状态设置为0
            AiClientConfig config = new AiClientConfig();
            config.setId(id);
            config.setStatus(0);
            config.setUpdateTime(LocalDateTime.now());

            int count = aiClientConfigDao.updateById(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}
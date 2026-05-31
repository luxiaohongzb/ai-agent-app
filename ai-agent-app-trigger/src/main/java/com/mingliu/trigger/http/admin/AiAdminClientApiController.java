package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.infrastructure.dao.IAiClientApiDao;
import com.mingliu.infrastructure.dao.IAiClientConfigDao;
import com.mingliu.infrastructure.dao.po.AiClientApi;
import com.mingliu.infrastructure.dao.po.AiClientConfig;
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
 * @Title: AiAdminClientApiController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 21:42
 * @description: API配置管理
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/api/")
@Tag(name = "API配置管理", description = "提供API配置的增删改查接口")
public class AiAdminClientApiController {
    @Resource
    private IAiClientApiDao aiClientApiDao;


    /**
     * 分页查询API配置列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询API配置列表", description = "分页查询所有API配置信息")
    @PostMapping("queryClientApiList")
    public ResponseEntity<PageResponse<AiClientApi>> queryClientApiList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClientApi> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClientApi> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClientApi::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClientApi::getStatus, request.getStatus());
            }

            IPage<AiClientApi> apiPage = aiClientApiDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(apiPage));
        } catch (Exception e) {
            log.error("查询API配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询API配置
     *
     * @param id API配置ID
     * @return API配置
     */
    @Operation(summary = "根据ID查询API配置", description = "获取指定ID的API配置详细信息")
    @GetMapping("queryClientApiById")
    public ResponseEntity<AiClientApi> queryClientApiById(@Parameter(description = "API配置ID") @RequestParam("id") Long id) {
        try {
            AiClientApi clientApi = aiClientApiDao.selectById(id);
            return ResponseEntity.ok(clientApi);
        } catch (Exception e) {
            log.error("查询API配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据API ID查询API配置
     *
     * @param apiId API ID
     * @return API配置
     */
    @Operation(summary = "根据API ID查询API配置", description = "获取指定API ID的API配置详细信息")
    @GetMapping("queryClientApiByApiId")
    public ResponseEntity<AiClientApi> queryClientApiByApiId(@Parameter(description = "API ID") @RequestParam("apiId") String apiId) {
        try {
            AiClientApi clientApi = aiClientApiDao.selectOne(
                new LambdaQueryWrapper<AiClientApi>().eq(AiClientApi::getApiId, apiId)
            );
            return ResponseEntity.ok(clientApi);
        } catch (Exception e) {
            log.error("查询API配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增API配置
     *
     * @param clientApi API配置
     * @return 结果
     */
    @Operation(summary = "新增API配置", description = "创建新的API配置")
    @PostMapping("addClientApi")
    public ResponseEntity<Boolean> addClientApi(@Parameter(description = "API配置信息") @RequestBody AiClientApi clientApi) {
        try {
            clientApi.setCreateTime(LocalDateTime.now());
            clientApi.setUpdateTime(LocalDateTime.now());
            int count = aiClientApiDao.insert(clientApi);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增API配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新API配置
     *
     * @param clientApi API配置
     * @return 结果
     */
    @Operation(summary = "更新API配置", description = "更新现有API配置的信息")
    @PostMapping("updateClientApi")
    public ResponseEntity<Boolean> updateClientApi(@Parameter(description = "API配置信息") @RequestBody AiClientApi clientApi) {
        try {
            clientApi.setUpdateTime(LocalDateTime.now());
            int count = aiClientApiDao.updateById(clientApi);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新API配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除API配置
     *
     * @param id API配置ID
     * @return 结果
     */
    @Operation(summary = "删除API配置", description = "删除指定ID的API配置")
    @GetMapping("deleteClientApi")
    public ResponseEntity<Boolean> deleteClientApi(@Parameter(description = "API配置ID") @RequestParam("id") Long id) {
        try {
            int count = aiClientApiDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除API配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}
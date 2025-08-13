package com.mingliu.trigger.http.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientAdvisorConfigDao;
import com.mingliu.infrastructure.dao.po.AiClientAdvisorConfig;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.ClientAdvisorConfigQueryRequest;
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
 * 2025-05-06 16:42
 */
@Tag(name = "客户端顾问配置管理", description = "客户端顾问配置相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/advisor/config/")
public class AiAdminClientAdvisorConfigController {

    @Resource
    private IAiClientAdvisorConfigDao aiClientAdvisorConfigDao;

    @Operation(summary = "查询客户端顾问配置列表", description = "分页查询客户端顾问配置列表")
    @RequestMapping(value = "queryClientAdvisorConfigList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientAdvisorConfig>> queryClientAdvisorConfigList(
            @Parameter(description = "查询条件和分页参数", required = true) @RequestBody ClientAdvisorConfigQueryRequest request) {
        try {
            // 设置分页参数
            if (StringUtils.hasText(request.getOrderBy())) {
                PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            } else {
                PageHelper.startPage(request.getPageNum(), request.getPageSize());
            }
            
            // 执行查询
            AiClientAdvisorConfig queryCondition = request.toAiClientAdvisorConfig();
            List<AiClientAdvisorConfig> configList = aiClientAdvisorConfigDao.queryClientAdvisorConfigList(queryCondition);
            
            // 包装分页结果
            PageInfo<AiClientAdvisorConfig> pageInfo = new PageInfo<>(configList);
            PageResponse<AiClientAdvisorConfig> pageResponse = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            log.error("查询客户端顾问配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询客户端顾问配置", description = "根据配置ID查询客户端顾问配置详细信息")
    @RequestMapping(value = "queryClientAdvisorConfigById", method = RequestMethod.GET)
    public ResponseEntity<AiClientAdvisorConfig> queryClientAdvisorConfigById(
            @Parameter(description = "客户端顾问配置ID", required = true) @RequestParam Long id) {
        try {
            AiClientAdvisorConfig config = aiClientAdvisorConfigDao.queryClientAdvisorConfigById(id);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            log.error("查询客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据客户端ID查询顾问配置列表", description = "根据客户端ID查询对应的顾问配置列表")
    @RequestMapping(value = "queryClientAdvisorConfigByClientId", method = RequestMethod.GET)
    public ResponseEntity<List<AiClientAdvisorConfig>> queryClientAdvisorConfigByClientId(
            @Parameter(description = "客户端ID", required = true) @RequestParam("clientId") Long clientId) {
        try {
            List<AiClientAdvisorConfig> configList = aiClientAdvisorConfigDao.queryClientAdvisorConfigByClientId(clientId);
            return ResponseEntity.ok(configList);
        } catch (Exception e) {
            log.error("根据客户端ID查询顾问配置列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增客户端顾问配置", description = "添加新的客户端顾问配置")
    @RequestMapping(value = "addClientAdvisorConfig", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addClientAdvisorConfig(
            @Parameter(description = "客户端顾问配置信息", required = true) @RequestBody AiClientAdvisorConfig aiClientAdvisorConfig) {
        try {
            aiClientAdvisorConfig.setCreateTime(new Date());
            int count = aiClientAdvisorConfigDao.insert(aiClientAdvisorConfig);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新客户端顾问配置", description = "更新现有客户端顾问配置信息")
    @RequestMapping(value = "updateClientAdvisorConfig", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateClientAdvisorConfig(
            @Parameter(description = "客户端顾问配置信息", required = true) @RequestBody AiClientAdvisorConfig aiClientAdvisorConfig) {
        try {
            int count = aiClientAdvisorConfigDao.update(aiClientAdvisorConfig);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除客户端顾问配置", description = "根据ID删除客户端顾问配置")
    @RequestMapping(value = "deleteClientAdvisorConfig", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteClientAdvisorConfig(
            @Parameter(description = "客户端顾问配置ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiClientAdvisorConfigDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端顾问配置异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

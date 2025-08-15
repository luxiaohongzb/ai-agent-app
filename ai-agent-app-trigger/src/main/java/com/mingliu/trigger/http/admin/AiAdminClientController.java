package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingliu.infrastructure.dao.IAiAgentDao;
import com.mingliu.infrastructure.dao.IAiAgentFlowConfigDao;
import com.mingliu.infrastructure.dao.IAiClientDao;
import com.mingliu.infrastructure.dao.po.AiAgent;
import com.mingliu.infrastructure.dao.po.AiAgentFlowConfig;
import com.mingliu.infrastructure.dao.po.AiClient;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: AiAdminClientController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 22:28
 * @description: 客户端管理服务
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/")
@Tag(name = "AI客户端管理", description = "提供客户端的增删改查接口")
public class AiAdminClientController {

    @Resource
    private IAiClientDao aiAgentClientDao;

    @Resource
    private IAiAgentFlowConfigDao aiAgentFlowConfigDao;
    /**
     * 分页查询客户端列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询客户端列表", description = "分页查询所有客户端信息")
    @PostMapping("queryClientList")
    public ResponseEntity<PageResponse<AiClient>> queryClientList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiClient> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiClient> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiClient::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiClient::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiClient::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiClient::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiClient> clientPage = aiAgentClientDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(clientPage));
        } catch (Exception e) {
            log.error("查询客户端列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询客户端
     *
     * @param id 客户端ID
     * @return 客户端
     */
    @Operation(summary = "根据ID查询客户端", description = "获取指定ID的客户端详细信息")
    @GetMapping("queryClientById")
    public ResponseEntity<AiClient> queryClientById(@Parameter(description = "客户端ID") @RequestParam("id") Long id) {
        try {
            AiClient client = aiAgentClientDao.selectById(id);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            log.error("查询客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据智能体ID查询关联的客户端
     *
     * @param agentId 智能体ID
     * @return 客户端列表
     */
    @Operation(summary = "根据智能体ID查询关联的客户端", description = "获取与指定智能体关联的所有客户端信息")
    @GetMapping("queryClientByAgentId")
    public ResponseEntity<List<AiClient>> queryClientByAgentId(@Parameter(description = "智能体ID") @RequestParam("agentId") String agentId) {
        try {
            // 1. 根据智能体ID查询关联配置
            List<AiAgentFlowConfig> aiAgentFlowConfigs = aiAgentFlowConfigDao.queryByAgentId(agentId);
            if (aiAgentFlowConfigs == null || aiAgentFlowConfigs.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            // 2. 获取所有关联的客户端ID
            List<String> clientIds = aiAgentFlowConfigs.stream()
                    .map(AiAgentFlowConfig::getClientId)
                    .collect(Collectors.toList());

            // 3. 批量查询客户端信息
            List<AiClient> clientList = aiAgentClientDao.selectBatchIds(clientIds);
            return ResponseEntity.ok(clientList);
        } catch (Exception e) {
            log.error("根据智能体ID查询客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端
     *
     * @param aiClient 客户端
     * @return 结果
     */
    @Operation(summary = "新增客户端", description = "创建新的客户端")
    @PostMapping("addClient")
    public ResponseEntity<Boolean> addClient(@Parameter(description = "客户端信息") @RequestBody AiClient aiClient) {
        try {
            aiClient.setCreateTime(LocalDateTime.now());
            int count = aiAgentClientDao.insert(aiClient);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端
     *
     * @param aiClient 客户端
     * @return 结果
     */
    @Operation(summary = "更新客户端", description = "更新现有客户端的信息")
    @PostMapping("updateClient")
    public ResponseEntity<Boolean> updateClient(@Parameter(description = "客户端信息") @RequestBody AiClient aiClient) {
        try {
            int count = aiAgentClientDao.updateById(aiClient);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除客户端
     *
     * @param id 客户端ID
     * @return 结果
     */
    @Operation(summary = "删除客户端", description = "删除指定ID的客户端")
    @GetMapping("deleteClient")
    public ResponseEntity<Boolean> deleteClient(@Parameter(description = "客户端ID") @RequestParam("id") Long id) {
        try {
            int count = aiAgentClientDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

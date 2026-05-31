package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiAgentFlowConfigDao;
import com.mingliu.infrastructure.dao.po.AiAgentFlowConfig;
import com.mingliu.trigger.http.dto.PageResponse;
import com.mingliu.trigger.http.vo.AiAgentClient;
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

/**
 * @Title: AiAdminAgentClientController
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 20:48
 * @description: AI智能体客户端关联控制器
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/agent/client/")
@Tag(name = "AI智能体客户端关联管理", description = "提供智能体和客户端关联的增删改查接口")
public class AiAdminAgentClientController {

    @Resource
    private IAiAgentFlowConfigDao aiAgentFlowConfigDao;

    /**
     * 分页查询AI智能体客户端关联列表
     *
     * @param aiAgentClient 查询条件
     * @return 分页结果
     */
    @PostMapping("queryAgentClientList")
    @Operation(summary = "分页查询AI智能体客户端关联列表", description = "根据条件分页查询智能体和客户端的关联关系")
    public ResponseEntity<PageResponse<AiAgentClient>> queryAgentClientList(@RequestBody AiAgentClient aiAgentClient) {
        try {
            // 设置分页参数
            PageHelper.startPage(aiAgentClient.getPageNum(), aiAgentClient.getPageSize());
            if (aiAgentClient.getOrderBy() != null && !aiAgentClient.getOrderBy().isEmpty()) {
                PageHelper.orderBy(aiAgentClient.getOrderBy());
            }

            // 执行查询
            List<AiAgentFlowConfig> configList = aiAgentFlowConfigDao.selectList(null);
            
            // 转换为VO对象
            List<AiAgentClient> voList = configList.stream()
                    .map(this::convertToVo)
                    .collect(Collectors.toList());

            // 封装分页信息
            PageInfo<AiAgentClient> pageInfo = new PageInfo<>(voList);
            return ResponseEntity.ok(PageResponse.of(pageInfo));
        } catch (Exception e) {
            log.error("查询AI智能体客户端关联列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询AI智能体客户端关联
     *
     * @param id AI智能体客户端关联ID
     * @return AI智能体客户端关联
     */
    @GetMapping("queryAgentClientById")
    @Operation(summary = "根据ID查询AI智能体客户端关联", description = "通过关联ID查询单个智能体和客户端的关联关系")
    public ResponseEntity<AiAgentClient> queryAgentClientById(@Parameter(description = "关联ID") @RequestParam("id") Long id) {
        try {
            AiAgentFlowConfig config = aiAgentFlowConfigDao.selectById(id);
            if (config == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertToVo(config));
        } catch (Exception e) {
            log.error("查询AI智能体客户端关联异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据智能体ID查询客户端关联列表
     *
     * @param agentId 智能体ID
     * @return 客户端关联列表
     */
    @GetMapping("queryAgentClientByAgentId")
    @Operation(summary = "根据智能体ID查询客户端关联列表", description = "查询指定智能体关联的所有客户端信息")
    public ResponseEntity<List<AiAgentClient>> queryAgentClientByAgentId(@Parameter(description = "智能体ID") @RequestParam("agentId") String agentId) {
        try {
            List<AiAgentFlowConfig> configList = aiAgentFlowConfigDao.queryByAgentId(agentId);
            List<AiAgentClient> voList = configList.stream()
                    .map(this::convertToVo)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(voList);
        } catch (Exception e) {
            log.error("根据智能体ID查询客户端关联列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增AI智能体客户端关联
     *
     * @param aiAgentClient AI智能体客户端关联
     * @return 结果
     */
    @PostMapping("addAgentClient")
    @Operation(summary = "新增AI智能体客户端关联", description = "创建新的智能体和客户端关联关系")
    public ResponseEntity<Boolean> addAgentClient(@RequestBody AiAgentClient aiAgentClient) {
        try {
            AiAgentFlowConfig config = convertToPo(aiAgentClient);
            config.setCreateTime(LocalDateTime.now());
            int count = aiAgentFlowConfigDao.insert(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增AI智能体客户端关联异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新AI智能体客户端关联
     *
     * @param aiAgentClient AI智能体客户端关联
     * @return 结果
     */
    @PostMapping("updateAgentClient")
    @Operation(summary = "更新AI智能体客户端关联", description = "更新已有的智能体和客户端关联关系")
    public ResponseEntity<Boolean> updateAgentClient(@RequestBody AiAgentClient aiAgentClient) {
        try {
            AiAgentFlowConfig config = convertToPo(aiAgentClient);
            int count = aiAgentFlowConfigDao.updateById(config);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新AI智能体客户端关联异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除AI智能体客户端关联
     *
     * @param id AI智能体客户端关联ID
     * @return 结果
     */
    @GetMapping("deleteAgentClient")
    @Operation(summary = "删除AI智能体客户端关联", description = "删除指定的智能体和客户端关联关系")
    public ResponseEntity<Boolean> deleteAgentClient(@Parameter(description = "关联ID") @RequestParam("id") Long id) {
        try {
            int count = aiAgentFlowConfigDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除AI智能体客户端关联异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 将PO对象转换为VO对象
     */
    private AiAgentClient convertToVo(AiAgentFlowConfig po) {
        AiAgentClient vo = new AiAgentClient();
        vo.setId(po.getId());
        vo.setAgentId(Long.valueOf(po.getAgentId()));
        vo.setClientId(Long.valueOf(po.getClientId()));
        vo.setSequence(po.getSequence());
        vo.setCreateTime(java.util.Date.from(po.getCreateTime().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        return vo;
    }

    /**
     * 将VO对象转换为PO对象
     */
    private AiAgentFlowConfig convertToPo(AiAgentClient vo) {
        AiAgentFlowConfig po = new AiAgentFlowConfig();
        po.setId(vo.getId());
        po.setAgentId(String.valueOf(vo.getAgentId()));
        po.setClientId(String.valueOf(vo.getClientId()));
        po.setSequence(vo.getSequence());
        if (vo.getCreateTime() != null) {
            po.setCreateTime(LocalDateTime.ofInstant(vo.getCreateTime().toInstant(), java.time.ZoneId.systemDefault()));
        }
        return po;
    }
}

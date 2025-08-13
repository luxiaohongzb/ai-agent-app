package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiAgentDao;
import com.mingliu.infrastructure.dao.po.AiAgent;
import com.mingliu.trigger.http.dto.BaseQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.dom.datetime.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 14:05
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/agent/")
@Tag(name = "AI智能体管理", description = "AI智能体相关接口")
public class AiAdminAgentController {

    @Resource
    private IAiAgentDao aiAgentDao;

    /**
     * 分页查询AI智能体列表
     * @param request 分页参数
     * @return 分页结果
     */
    @Operation(summary = "查询AI智能体列表", description = "分页查询AI智能体列表")
    @RequestMapping(value = "queryAiAgentList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiAgent>> queryAiAgentList(@RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            if (StringUtils.hasText(request.getOrderBy())) {
                PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            } else {
                PageHelper.startPage(request.getPageNum(), request.getPageSize());
            }
            
            // 执行查询
            List<AiAgent> aiAgentList = aiAgentDao.queryAll();
            
            // 包装分页结果
            PageInfo<AiAgent> pageInfo = new PageInfo<>(aiAgentList);
            PageResponse<AiAgent> pageResponse = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            log.error("查询AI智能体列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据渠道查询智能体配置", description = "根据指定渠道获取智能体配置列表")
    @RequestMapping(value = "queryAllAgentConfigListByChannel", method = RequestMethod.POST)
    public ResponseEntity<List<AiAgent>> queryAllAgentConfig(
            @Parameter(description = "渠道名称", required = true) @RequestParam("channel") String channel) {
        try {
            List<AiAgent> aiAgentList = aiAgentDao.queryByChannel(channel);
            return ResponseEntity.ok(aiAgentList);
        } catch (Exception e) {
            log.error("查询AI智能体列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询AI智能体
     *
     * @param id AI智能体ID
     * @return AI智能体
     */
    @Operation(summary = "根据ID查询AI智能体", description = "根据智能体ID获取详细信息")
    @RequestMapping(value = "queryAiAgentById", method = RequestMethod.GET)
    public ResponseEntity<AiAgent> queryAiAgentById(
            @Parameter(description = "智能体ID", required = true) @RequestParam("id") Long id) {
        try {
            AiAgent aiAgent = aiAgentDao.queryById(id);
            return ResponseEntity.ok(aiAgent);
        } catch (Exception e) {
            log.error("查询AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    @Operation(summary = "新增AI智能体", description = "创建新的AI智能体")
    @RequestMapping(value = "addAiAgent", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addAiAgent(
            @Parameter(description = "智能体信息", required = true) @RequestBody AiAgent aiAgent) {
        try {
            aiAgent.setCreateTime(LocalDateTime.now());
            aiAgent.setUpdateTime(LocalDateTime.now());
            int count = aiAgentDao.insert(aiAgent);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    @Operation(summary = "更新AI智能体", description = "更新现有AI智能体信息")
    @RequestMapping(value = "updateAiAgent", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateAiAgent(
            @Parameter(description = "智能体信息", required = true) @RequestBody AiAgent aiAgent) {
        try {
            aiAgent.setUpdateTime(LocalDateTime.now());
            int count = aiAgentDao.updateByAgentId(aiAgent);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除AI智能体
     *
     * @param id AI智能体ID
     * @return 结果
     */
    @Operation(summary = "删除AI智能体", description = "根据ID删除AI智能体")
    @RequestMapping(value = "deleteAiAgent", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteAiAgent(
            @Parameter(description = "智能体ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiAgentDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除AI智能体异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

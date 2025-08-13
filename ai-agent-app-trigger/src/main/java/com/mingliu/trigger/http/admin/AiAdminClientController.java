package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiAgentClientDao;
import com.mingliu.infrastructure.dao.po.AiAgentClient;
import com.mingliu.trigger.http.dto.ClientQueryRequest;
import com.mingliu.trigger.http.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 客户端管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 15:16
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/")
@Tag(name = "客户端管理", description = "客户端相关接口")
public class AiAdminClientController {

    @Resource
    private IAiAgentClientDao aiAgentClientDao;

    /**
     * 分页查询客户端列表
     *
     * @param request 查询条件和分页参数
     * @return 分页结果
     */
    @Operation(summary = "查询客户端列表", description = "分页查询客户端列表")
    @RequestMapping(value = "queryClientList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiAgentClient>> queryClientList(@RequestBody ClientQueryRequest request) {
        try {
            // 设置分页参数
            if (StringUtils.hasText(request.getOrderBy())) {
                PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            } else {
                PageHelper.startPage(request.getPageNum(), request.getPageSize());
            }
            
            // 执行查询
            AiAgentClient queryCondition = request.toAiAgentClient();
            List<AiAgentClient> clientList = aiAgentClientDao.queryAgentClientList(queryCondition);
            
            // 包装分页结果
            PageInfo<AiAgentClient> pageInfo = new PageInfo<>(clientList);
            PageResponse<AiAgentClient> pageResponse = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(pageResponse);
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
    @Operation(summary = "根据ID查询客户端", description = "根据客户端ID查询客户端详情")
    @RequestMapping(value = "queryClientById", method = RequestMethod.GET)
    public ResponseEntity<AiAgentClient> queryClientById(
            @Parameter(description = "客户端ID", required = true) @RequestParam("id") Long id) {
        try {
            AiAgentClient client = aiAgentClientDao.queryAgentClientConfigById(id);
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
    @Operation(summary = "根据智能体ID查询客户端", description = "根据智能体ID查询关联的客户端列表")
    @RequestMapping(value = "queryClientByAgentId", method = RequestMethod.GET)
    public ResponseEntity<List<AiAgentClient>> queryClientByAgentId(
            @Parameter(description = "智能体ID", required = true) @RequestParam("agentId") Long agentId) {
        try {
            List<AiAgentClient> clientList = aiAgentClientDao.queryAgentClientConfigByAgentId(agentId);
            return ResponseEntity.ok(clientList);
        } catch (Exception e) {
            log.error("根据智能体ID查询客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端
     *
     * @param aiAgentClient 客户端
     * @return 结果
     */
    @Operation(summary = "新增客户端", description = "新增客户端信息")
    @RequestMapping(value = "addClient", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addClient(@RequestBody AiAgentClient aiAgentClient) {
        try {
            aiAgentClient.setCreateTime(new Date());
            int count = aiAgentClientDao.insert(aiAgentClient);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端
     *
     * @param aiAgentClient 客户端
     * @return 结果
     */
    @Operation(summary = "更新客户端", description = "更新客户端信息")
    @RequestMapping(value = "updateClient", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateClient(@RequestBody AiAgentClient aiAgentClient) {
        try {
            int count = aiAgentClientDao.update(aiAgentClient);
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
    @Operation(summary = "删除客户端", description = "根据ID删除客户端")
    @RequestMapping(value = "deleteClient", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteClient(
            @Parameter(description = "客户端ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiAgentClientDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

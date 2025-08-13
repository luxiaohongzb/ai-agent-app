package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiAgentTaskScheduleDao;
import com.mingliu.infrastructure.dao.po.AiAgentTaskSchedule;
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
 * AI代理任务调度管理服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 16:25
 */
@Tag(name = "AI代理任务调度管理", description = "AI代理任务调度相关接口")
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/agent/task/")
public class AiAdminAgentTaskScheduleController {

    @Resource
    private IAiAgentTaskScheduleDao aiAgentTaskScheduleDao;

    @Operation(summary = "查询AI代理任务调度列表", description = "获取所有AI代理任务调度列表")
    @RequestMapping(value = "queryTaskScheduleList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiAgentTaskSchedule>> queryTaskScheduleList(
            @Parameter(description = "查询条件", required = true) @RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            
            List<AiAgentTaskSchedule> taskScheduleList = aiAgentTaskScheduleDao.queryAll();
            
            // 包装分页结果
            PageInfo<AiAgentTaskSchedule> pageInfo = new PageInfo<>(taskScheduleList);
            PageResponse<AiAgentTaskSchedule> response = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询AI代理任务调度列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "根据ID查询AI代理任务调度", description = "根据任务调度ID查询详细信息")
    @RequestMapping(value = "queryTaskScheduleById", method = RequestMethod.GET)
    public ResponseEntity<AiAgentTaskSchedule> queryTaskScheduleById(
            @Parameter(description = "AI代理任务调度ID", required = true) @RequestParam("id") Long id) {
        try {
            AiAgentTaskSchedule taskSchedule = aiAgentTaskScheduleDao.queryById(id);
            return ResponseEntity.ok(taskSchedule);
        } catch (Exception e) {
            log.error("查询AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "新增AI代理任务调度", description = "添加新的AI代理任务调度")
    @RequestMapping(value = "addTaskSchedule", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addTaskSchedule(
            @Parameter(description = "AI代理任务调度信息", required = true) @RequestBody AiAgentTaskSchedule aiAgentTaskSchedule) {
        try {
            aiAgentTaskSchedule.setCreateTime(LocalDateTime.now());
            aiAgentTaskSchedule.setUpdateTime(LocalDateTime.now());
            int count = aiAgentTaskScheduleDao.insert(aiAgentTaskSchedule);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "更新AI代理任务调度", description = "更新现有AI代理任务调度信息")
    @RequestMapping(value = "updateTaskSchedule", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateTaskSchedule(
            @Parameter(description = "AI代理任务调度信息", required = true) @RequestBody AiAgentTaskSchedule aiAgentTaskSchedule) {
        try {
            aiAgentTaskSchedule.setUpdateTime(LocalDateTime.now());
            int count = aiAgentTaskScheduleDao.updateById(aiAgentTaskSchedule);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "删除AI代理任务调度", description = "根据ID删除AI代理任务调度")
    @RequestMapping(value = "deleteTaskSchedule", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteTaskSchedule(
            @Parameter(description = "AI代理任务调度ID", required = true) @RequestParam("id") Long id) {
        try {
            int count = aiAgentTaskScheduleDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

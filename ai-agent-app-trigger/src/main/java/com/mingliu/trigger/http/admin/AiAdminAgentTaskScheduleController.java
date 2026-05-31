package com.mingliu.trigger.http.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Title: IAiAgentTaskScheduleDao
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.admin
 * @Date 2025/8/15 21:52
 * @description: agent调度类
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/agent/task/")
@Tag(name = "智能体任务调度管理", description = "提供智能体任务调度的增删改查接口")
public class AiAdminAgentTaskScheduleController {
    @Resource
    private IAiAgentTaskScheduleDao aiAgentTaskScheduleDao;

    /**
     * 分页查询AI代理任务调度列表
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @Operation(summary = "查询任务调度列表", description = "分页查询所有任务调度信息")
    @PostMapping("queryTaskScheduleList")
    public ResponseEntity<PageResponse<AiAgentTaskSchedule>> queryTaskScheduleList(@Parameter(description = "查询条件") @RequestBody BaseQueryRequest request) {
        try {
            Page<AiAgentTaskSchedule> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<AiAgentTaskSchedule> wrapper = new LambdaQueryWrapper<>();
            if (request.getId() != null) {
                wrapper.eq(AiAgentTaskSchedule::getId, request.getId());
            }
            if (request.getStatus() != null) {
                wrapper.eq(AiAgentTaskSchedule::getStatus, request.getStatus());
            }
            if (request.getCreateTimeStart() != null) {
                wrapper.ge(AiAgentTaskSchedule::getCreateTime, request.getCreateTimeStart());
            }
            if (request.getCreateTimeEnd() != null) {
                wrapper.le(AiAgentTaskSchedule::getCreateTime, request.getCreateTimeEnd());
            }
            IPage<AiAgentTaskSchedule> taskSchedulePage = aiAgentTaskScheduleDao.selectPage(page, wrapper);
            return ResponseEntity.ok(PageResponse.of(taskSchedulePage));
        } catch (Exception e) {
            log.error("查询AI代理任务调度列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询AI代理任务调度
     *
     * @param id AI代理任务调度ID
     * @return AI代理任务调度
     */
    @Operation(summary = "根据ID查询任务调度", description = "获取指定ID的任务调度详细信息")
    @GetMapping("queryTaskScheduleById")
    public ResponseEntity<AiAgentTaskSchedule> queryTaskScheduleById(@Parameter(description = "任务调度ID") @RequestParam("id") Long id) {
        try {
            AiAgentTaskSchedule taskSchedule = aiAgentTaskScheduleDao.queryTaskScheduleById(id);
            return ResponseEntity.ok(taskSchedule);
        } catch (Exception e) {
            log.error("查询AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增AI代理任务调度
     *
     * @param aiAgentTaskSchedule AI代理任务调度
     * @return 结果
     */
    @Operation(summary = "新增任务调度", description = "创建新的任务调度")
    @PostMapping("addTaskSchedule")
    public ResponseEntity<Boolean> addTaskSchedule(@Parameter(description = "任务调度信息") @RequestBody AiAgentTaskSchedule aiAgentTaskSchedule) {
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

    /**
     * 更新AI代理任务调度
     *
     * @param aiAgentTaskSchedule AI代理任务调度
     * @return 结果
     */
    @Operation(summary = "更新任务调度", description = "更新现有任务调度的信息")
    @PostMapping("updateTaskSchedule")
    public ResponseEntity<Boolean> updateTaskSchedule(@Parameter(description = "任务调度信息") @RequestBody AiAgentTaskSchedule aiAgentTaskSchedule) {
        try {
            aiAgentTaskSchedule.setUpdateTime(LocalDateTime.now());
            int count = aiAgentTaskScheduleDao.updateById(aiAgentTaskSchedule);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除AI代理任务调度
     *
     * @param id AI代理任务调度ID
     * @return 结果
     */
    @Operation(summary = "删除任务调度", description = "删除指定ID的任务调度")
    @GetMapping("deleteTaskSchedule")
    public ResponseEntity<Boolean> deleteTaskSchedule(@Parameter(description = "任务调度ID") @RequestParam("id") Long id) {
        try {
            int count = aiAgentTaskScheduleDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除AI代理任务调度异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

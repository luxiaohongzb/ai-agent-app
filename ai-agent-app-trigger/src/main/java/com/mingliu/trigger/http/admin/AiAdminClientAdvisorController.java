package com.mingliu.trigger.http.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingliu.infrastructure.dao.IAiClientAdvisorDao;
import com.mingliu.infrastructure.dao.po.AiClientAdvisor;
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
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-06 15:45
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ai/admin/client/advisor/")
@Tag(name = "客户端顾问管理", description = "客户端顾问相关接口")
public class AiAdminClientAdvisorController {

    @Resource
    private IAiClientAdvisorDao aiClientAdvisorDao;

    /**
     * 分页查询客户端顾问列表
     *
     * @param request 分页参数
     * @return 分页结果
     */
    @Operation(summary = "查询客户端顾问列表", description = "分页查询客户端顾问列表")
    @RequestMapping(value = "queryClientAdvisorList", method = RequestMethod.POST)
    public ResponseEntity<PageResponse<AiClientAdvisor>> queryClientAdvisorList(@RequestBody BaseQueryRequest request) {
        try {
            // 设置分页参数
            if (StringUtils.hasText(request.getOrderBy())) {
                PageHelper.startPage(request.getPageNum(), request.getPageSize(), request.getOrderBy());
            } else {
                PageHelper.startPage(request.getPageNum(), request.getPageSize());
            }
            
            // 执行查询
            List<AiClientAdvisor> aiClientAdvisorList = aiClientAdvisorDao.queryAll();
            
            // 包装分页结果
            PageInfo<AiClientAdvisor> pageInfo = new PageInfo<>(aiClientAdvisorList);
            PageResponse<AiClientAdvisor> pageResponse = PageResponse.of(pageInfo);
            
            return ResponseEntity.ok(pageResponse);
        } catch (Exception e) {
            log.error("查询客户端顾问列表异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID查询客户端顾问
     *
     * @param id 客户端顾问ID
     * @return 客户端顾问
     */
    @RequestMapping(value = "queryClientAdvisorById", method = RequestMethod.GET)
    public ResponseEntity<AiClientAdvisor> queryClientAdvisorById(@RequestParam("id") Long id) {
        try {
            AiClientAdvisor aiClientAdvisor = aiClientAdvisorDao.queryById(id);
            return ResponseEntity.ok(aiClientAdvisor);
        } catch (Exception e) {
            log.error("查询客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 新增客户端顾问
     *
     * @param aiClientAdvisor 客户端顾问
     * @return 结果
     */
    @RequestMapping(value = "addClientAdvisor", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addClientAdvisor(@RequestBody AiClientAdvisor aiClientAdvisor) {
        try {
            aiClientAdvisor.setCreateTime(LocalDateTime.now());
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            int count = aiClientAdvisorDao.insert(aiClientAdvisor);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("新增客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新客户端顾问
     *
     * @param aiClientAdvisor 客户端顾问
     * @return 结果
     */
    @RequestMapping(value = "updateClientAdvisor", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateClientAdvisor(@RequestBody AiClientAdvisor aiClientAdvisor) {
        try {
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            int count = aiClientAdvisorDao.updateByAdvisorId(aiClientAdvisor);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("更新客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 删除客户端顾问
     *
     * @param id 客户端顾问ID
     * @return 结果
     */
    @RequestMapping(value = "deleteClientAdvisor", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteClientAdvisor(@RequestParam("id") Long id) {
        try {
            int count = aiClientAdvisorDao.deleteById(id);
            return ResponseEntity.ok(count > 0);
        } catch (Exception e) {
            log.error("删除客户端顾问异常", e);
            return ResponseEntity.status(500).build();
        }
    }
}

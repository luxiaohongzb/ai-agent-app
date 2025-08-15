package com.mingliu.trigger.http.vo;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Title: AiClientAdvisorConfig
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.vo
 * @Date 2025/8/15 23:57
 * @description: 客户端-顾问关联表
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AiClientAdvisorConfig {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 顾问ID
     */
    private Long advisorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
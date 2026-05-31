package com.mingliu.trigger.http.vo;

/**
 * @Title: AiClientModelConfig
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.vo
 * @Date 2025/8/16 0:20
 * @description: AI客户端模型配置表
 */

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI客户端模型配置表
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AiClientModelConfig {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 模型ID
     */
    private Long modelId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
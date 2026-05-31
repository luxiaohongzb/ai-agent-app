package com.mingliu.trigger.http.vo;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Title: AiClientToolConfig
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.vo
 * @Date 2025/8/15 23:32
 * @description: 客户端-工具关联表
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AiClientToolConfig {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 工具类型(mcp/function call)
     */
    private String toolType;

    /**
     * 工具ID(MCP ID/function call ID)
     */
    private Long toolId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

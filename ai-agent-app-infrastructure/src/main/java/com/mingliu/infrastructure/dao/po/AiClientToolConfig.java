package com.mingliu.infrastructure.dao.po;


import lombok.*;

import java.util.Date;

/**
 * 客户端-工具关联表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Date createTime;
}
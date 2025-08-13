package com.mingliu.infrastructure.dao.po;


import lombok.*;

import java.util.Date;

/**
 * 智能体-客户端关联表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentClient {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 智能体ID
     */
    private Long agentId;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 序列号(执行顺序)
     */
    private Integer sequence;

    /**
     * 创建时间
     */
    private Date createTime;

}
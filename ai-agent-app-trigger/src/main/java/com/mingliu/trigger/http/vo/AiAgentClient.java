package com.mingliu.trigger.http.vo;

import com.mingliu.trigger.http.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Title: aiAgentClient
 * @Author mingliu0608
 * @Package com.mingliu.trigger.http.vo
 * @Date 2025/8/15 20:55
 * @description: Ai智能体客户端关联
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiAgentClient extends PageRequest {
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
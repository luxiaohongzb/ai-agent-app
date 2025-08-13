package com.mingliu.infrastructure.dao.po;

import lombok.*;

import java.util.Date;

/**
 * 知识库配置表
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiRagOrder {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 知识库名称
     */
    private String ragName;

    /**
     * 知识标签
     */
    private String knowledgeTag;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
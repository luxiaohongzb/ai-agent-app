package com.mingliu.infrastructure.dao.po;


import lombok.*;

import java.util.Date;

/**
 * AI客户端模型配置表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Date createTime;

}
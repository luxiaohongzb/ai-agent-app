package com.mingliu.infrastructure.dao.po;


import lombok.*;

import java.util.Date;

/**
 * 客户端-顾问关联表
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Date createTime;
}
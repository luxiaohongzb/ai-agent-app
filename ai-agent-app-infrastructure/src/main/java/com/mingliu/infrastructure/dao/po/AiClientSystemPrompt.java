package com.mingliu.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统提示词配置表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("ai_client_system_prompt")
public class AiClientSystemPrompt {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 提示词ID
     */
    private String promptId;

    /**
     * 提示词名称
     */
    private String promptName;

    /**
     * 提示词内容
     */
    private String promptContent;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}

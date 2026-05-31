package com.mingliu.trigger.http.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 客户端顾问配置查询请求
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-01-XX XX:XX
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户端顾问配置查询请求")
public class ClientAdvisorConfigQueryRequest extends PageRequest {

    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "客户端ID")
    private Long clientId;

    @Schema(description = "顾问ID")
    private Long advisorId;

    @Schema(description = "状态")
    private Integer status;

    /**
     * 转换为AiClientAdvisorConfig对象
     * @return AiClientAdvisorConfig
     */
//    public AiClientAdvisorConfig toAiClientAdvisorConfig() {
//        AiClientAdvisorConfig config = new AiClientAdvisorConfig();
//        config.setId(this.id);
//        config.setClientId(this.clientId);
//        config.setAdvisorId(this.advisorId);
//        // 注意：AiClientAdvisorConfig实体类中没有status字段
//        // status字段仅用于查询条件，不直接映射到实体类
//        return config;
//    }
}
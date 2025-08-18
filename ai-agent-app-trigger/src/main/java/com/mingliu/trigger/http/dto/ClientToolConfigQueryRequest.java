package com.mingliu.trigger.http.dto;

//import com.mingliu.infrastructure.dao.po.AiClientToolConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 客户端工具配置查询请求
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-01-XX XX:XX
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户端工具配置查询请求")
public class ClientToolConfigQueryRequest extends PageRequest {

    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "客户端ID")
    private Long clientId;

    @Schema(description = "工具名称")
    private String toolName;

    @Schema(description = "状态")
    private Integer status;

    /**
     * 转换为AiClientToolConfig对象
     * @return AiClientToolConfig
     */
//    public AiClientToolConfig toAiClientToolConfig() {
//        AiClientToolConfig config = new AiClientToolConfig();
//        config.setId(this.id);
//        config.setClientId(this.clientId);
//        // 注意：AiClientToolConfig实体类中没有toolName和status字段
//        // 这些字段仅用于查询条件，不直接映射到实体类
//        return config;
//    }
}
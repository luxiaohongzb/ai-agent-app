package com.mingliu.trigger.http.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 客户端查询请求参数
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-01-XX XX:XX
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户端查询请求参数")
public class ClientQueryRequest extends PageRequest {

    @Schema(description = "客户端ID")
    private Long id;

    @Schema(description = "智能体ID")
    private Long agentId;

    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    /**
     * 转换为PO对象
     */
//    public AiAgentClient toAiAgentClient() {
//        AiAgentClient aiAgentClient = new AiAgentClient();
//        aiAgentClient.setId(this.id);
//        aiAgentClient.setAgentId(this.agentId);
//        return aiAgentClient;
//    }
}
package com.mingliu.trigger.http.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * RAG顺序查询请求参数
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-01-XX XX:XX
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "RAG顺序查询请求参数")
public class RagOrderQueryRequest extends PageRequest {

    @Schema(description = "RAG顺序ID")
    private Long id;

    @Schema(description = "客户端ID")
    private Long clientId;

    @Schema(description = "RAG名称")
    private String ragName;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "排序")
    private Integer sort;

    /**
     * 转换为PO对象
     */
//    public AiRagOrder toAiRagOrder() {
//        AiRagOrder aiRagOrder = new AiRagOrder();
//        aiRagOrder.setId(this.id);
//        aiRagOrder.setRagName(this.ragName);
//        // 注意：AiRagOrder实体类中没有clientId和sort字段
//        // 这些字段仅用于查询条件，不直接映射到实体类
//        // aiRagOrder.setStatus(this.status); // status字段类型不匹配，需要转换
//        if (this.status != null) {
//            try {
//                aiRagOrder.setStatus(Integer.valueOf(this.status));
//            } catch (NumberFormatException e) {
//                // 如果转换失败，可以设置默认值或忽略
//            }
//        }
//        return aiRagOrder;
//    }
}
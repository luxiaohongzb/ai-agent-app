package com.mingliu.domain.agent.model.entity;

import com.mingliu.domain.agent.model.valobj.enums.AiImageModelTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文生图申请实体
 * @author mingliu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiImageApplyEntity {

    /** 模型类型 */
    private AiImageModelTypeEnum model;
    
    /** 正向提示词 */
    private String prompt;
    
    /** 负向提示词 */
    private String negativePrompt;
    
    /** 图片尺寸 */
    private String size;

}

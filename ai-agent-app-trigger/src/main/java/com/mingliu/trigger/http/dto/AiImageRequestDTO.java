package com.mingliu.trigger.http.dto;

import lombok.Data;

/**
 * 文生图请求对象
 * @author mingliu
 */
@Data
public class AiImageRequestDTO {

    /** 提示词 */
    private String prompt;

    /** 模型类型: wanx-v1, qwen-image-max */
    private String model;

    /** 负向提示词 */
    private String negativePrompt;

    /** 图片尺寸 */
    private String size;

}

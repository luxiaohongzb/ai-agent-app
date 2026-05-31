package com.mingliu.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文生图模型类型枚举
 * @author mingliu
 */
@Getter
@AllArgsConstructor
public enum AiImageModelTypeEnum {

    WANX_V1("wanx-v1", "通义万相-v1"),
    QWEN_IMAGE_MAX("qwen-image-max", "通义千问-Image-Max"),
    ;

    private final String code;
    private final String info;

    public static AiImageModelTypeEnum getByCode(String code) {
        for (AiImageModelTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

}

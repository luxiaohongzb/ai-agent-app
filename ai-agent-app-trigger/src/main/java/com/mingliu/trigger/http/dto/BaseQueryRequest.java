package com.mingliu.trigger.http.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 基础查询请求参数
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-01-XX XX:XX
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "基础查询请求参数")
public class BaseQueryRequest extends PageRequest {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建时间开始")
    private String createTimeStart;

    @Schema(description = "创建时间结束")
    private String createTimeEnd;
}
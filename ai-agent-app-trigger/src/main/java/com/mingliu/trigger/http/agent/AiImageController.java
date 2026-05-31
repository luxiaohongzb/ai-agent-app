package com.mingliu.trigger.http.agent;

import com.mingliu.api.response.Response;
import com.mingliu.domain.agent.model.entity.AiImageApplyEntity;
import com.mingliu.domain.agent.model.valobj.enums.AiImageModelTypeEnum;
import com.mingliu.domain.agent.service.IAiImageService;
import com.mingliu.trigger.http.dto.AiImageRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 文生图controller
 *
 * @author mingliu
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/image")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@Tag(name = "文生图接口", description = "提供文生图对话接口")
public class AiImageController {

    @Resource
    private IAiImageService aiImageService;

    @Operation(summary = "文生图", description = "根据提示词生成图片")
    @RequestMapping(value = "text_to_image", method = RequestMethod.POST)
    public Response<String> textToImage(@RequestBody AiImageRequestDTO request) {
        try {
            log.info("文生图请求 request:{}", request);
            
            // 构建请求实体
            AiImageApplyEntity applyEntity = AiImageApplyEntity.builder()
                    .prompt(request.getPrompt())
                    .negativePrompt(StringUtils.isNotBlank(request.getNegativePrompt()) ? request.getNegativePrompt() : "低分辨率，低画质，肢体畸形，手指畸形，画面过饱和，蜡像感，人脸无细节，过度光滑，画面具有AI感。构图混乱。文字模糊，扭曲。")
                    .size(StringUtils.isNotBlank(request.getSize()) ? request.getSize() : "1024*1024")
                    .build();
            
            // 设置模型，默认使用 QWEN_IMAGE_MAX
            if (StringUtils.isNotBlank(request.getModel())) {
                AiImageModelTypeEnum modelEnum = AiImageModelTypeEnum.getByCode(request.getModel());
                if (modelEnum != null) {
                    applyEntity.setModel(modelEnum);
                } else {
                    applyEntity.setModel(AiImageModelTypeEnum.QWEN_IMAGE_MAX);
                }
            } else {
                applyEntity.setModel(AiImageModelTypeEnum.QWEN_IMAGE_MAX);
            }
            
            String url = aiImageService.textToImage(applyEntity);
            return Response.success(url);
        } catch (Exception e) {
            log.error("文生图请求异常 request:{}", request, e);
            return Response.fail("生成失败：" + e.getMessage());
        }
    }
}

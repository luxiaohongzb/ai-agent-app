package com.mingliu.domain.agent.service.image;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.mingliu.domain.agent.model.entity.AiImageApplyEntity;
import com.mingliu.domain.agent.model.valobj.enums.AiImageModelTypeEnum;
import com.mingliu.domain.agent.service.IAiImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 文生图服务实现
 *
 * @author mingliu
 */
@Slf4j
@Service
public class AiImageService implements IAiImageService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Override
    public String textToImage(AiImageApplyEntity request) {
        try {
            // 根据模型类型选择不同的调用方式
            if (AiImageModelTypeEnum.WANX_V1.equals(request.getModel())) {
                return textToImageWanx(request);
            } else {
                // 默认或指定 qwen-image-max
                return textToImageQwen(request);
            }
        } catch (Exception e) {
            log.error("文生图异常 request:{}", request, e);
            throw new RuntimeException("文生图生成失败: " + e.getMessage(), e);
        }
    }

    private String textToImageWanx(AiImageApplyEntity request) throws ApiException, NoApiKeyException {
        // 构建请求参数，使用 wanx-v1 模型
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model(AiImageModelTypeEnum.WANX_V1.getCode())
                        .prompt(request.getPrompt())
                        .n(1)
                        .size(StringUtils.isNotBlank(request.getSize()) ? request.getSize() : "1024*1024")
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = imageSynthesis.call(param);

        if (result.getOutput() != null && result.getOutput().getResults() != null && !result.getOutput().getResults().isEmpty()) {
            return result.getOutput().getResults().get(0).get("url");
        }
        return null;
    }

    private String textToImageQwen(AiImageApplyEntity request) throws ApiException, NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();

        // 构建用户消息
        MultiModalMessage userMessage = MultiModalMessage.builder()
                .role(Role.USER.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("text", request.getPrompt())
                )).build();

        // 设置参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("watermark", false);
        parameters.put("prompt_extend", true);
        
        // 负向提示词
        parameters.put("negative_prompt", request.getNegativePrompt());
        
        // 尺寸
        parameters.put("size", request.getSize());

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(apiKey)
                .model(AiImageModelTypeEnum.QWEN_IMAGE_MAX.getCode())
                .messages(Collections.singletonList(userMessage))
                .parameters(parameters)
                .build();

        MultiModalConversationResult result = conv.call(param);

        // 解析结果
        if (result.getOutput() != null && result.getOutput().getChoices() != null && !result.getOutput().getChoices().isEmpty()) {
            var choice = result.getOutput().getChoices().get(0);
            if (choice.getMessage() != null && choice.getMessage().getContent() != null && !choice.getMessage().getContent().isEmpty()) {
                var contentItem = choice.getMessage().getContent().get(0);
                if (contentItem instanceof Map) {
                    Map<String, Object> contentMap = (Map<String, Object>) contentItem;
                    if (contentMap.containsKey("image")) {
                        return (String) contentMap.get("image");
                    }
                }
            }
        }
        return null;
    }
}

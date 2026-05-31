package com.mingliu.domain.agent.service;

import com.mingliu.domain.agent.model.entity.AiImageApplyEntity;

/**
 * 文生图服务接口
 * @author mingliu
 */
public interface IAiImageService {

    /**
     * 文生图
     *
     * @param request 文生图申请实体
     * @return 图片URL
     */
    String textToImage(AiImageApplyEntity request);

}

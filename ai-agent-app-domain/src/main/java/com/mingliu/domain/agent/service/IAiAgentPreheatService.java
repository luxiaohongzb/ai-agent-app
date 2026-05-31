package com.mingliu.domain.agent.service;

import javax.validation.constraints.DecimalMax;
import java.util.List;

/**
 * AiAgent 装配服务接口
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-02 13:26
 */
public interface IAiAgentPreheatService {

    /**
     * 服务预热，启动时触达
     */
    void preheat() throws Exception;

    void preheat(String aiClientId) throws Exception;
    void preheat(List<String> aiClientIds) throws Exception;
}

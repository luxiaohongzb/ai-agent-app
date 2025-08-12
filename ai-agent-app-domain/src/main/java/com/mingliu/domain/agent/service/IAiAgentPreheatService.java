package com.mingliu.domain.agent.service;

/**
 * @Title: IAiAgentChatService
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service
 * @Date 2025/8/10 22:33
 * @description:
 */
public interface IAiAgentPreheatService {
    /**
     * 服务预热，启动时触达
     */
    void preheat() throws Exception;

    void preheat(Long aiClientId) throws Exception;
}

package com.mingliu.domain.agent.service.preheat;

import com.mingliu.domain.agent.service.IAiAgentPreheatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: AiAgentPreheatService
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.preheat
 * @Date 2025/8/10 22:35
 * @description: ai热装配所有组件
 */


@Slf4j
@Service
public class AiAgentPreheatService implements IAiAgentPreheatService {
    /**
     * 服务预热，启动时触达
     */
    @Override
    public void preheat() throws Exception {

    }

    @Override
    public void preheat(Long aiClientId) throws Exception {

    }
}

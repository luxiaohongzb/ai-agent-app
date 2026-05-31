package com.mingliu.api;

import com.mingliu.api.dto.AutoAgentRequestDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * @Title: IAiAgentService
 * @Author mingliu0608
 * @Package com.mingliu.api
 * @Date 2025/8/16 20:09
 * @description: agent service
 */
public interface IAiAgentService {

    ResponseBodyEmitter autoAgent(AutoAgentRequestDTO request, HttpServletResponse response);
}

package com.mingliu.domain.agent.service.execute.auto.step;

import com.mingliu.domain.agent.model.entity.ExecuteCommandEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * @Title: IExecuteStrategy
 * @Author mingliu0608
 * @Package com.mingliu.domain.agent.service.execute.auto.step
 * @Date 2025/8/15 19:04
 * @description:
 */
public interface IExecuteStrategy {
    void execute(ExecuteCommandEntity requestParameter, ResponseBodyEmitter emitter) throws Exception;
}

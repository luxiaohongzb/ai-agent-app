package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientSystemPrompt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端系统提示词数据访问层
 */
@Mapper
public interface IAiClientSystemPromptDao extends BaseMapper<AiClientSystemPrompt> {


    @Select("SELECT * FROM ai_client_system_prompt WHERE prompt_id = #{promptId} AND status = 1")
    AiClientSystemPrompt queryByPromptId(String promptId);

}
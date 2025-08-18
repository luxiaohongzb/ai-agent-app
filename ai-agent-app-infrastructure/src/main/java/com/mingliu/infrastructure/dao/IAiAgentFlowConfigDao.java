package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiAgentFlowConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 智能体流程配置数据访问层
 */
@Mapper
public interface IAiAgentFlowConfigDao extends BaseMapper<AiAgentFlowConfig> {

    /**
     * 根据智能体ID查询流程配置
     * @param agentId 智能体ID
     * @return 流程配置列表
     */

    @Select("SELECT * from `ai-agent-station`.ai_agent_flow_config where agent_id = #{agentId}")
    List<AiAgentFlowConfig> queryByAgentId(String agentId);

}
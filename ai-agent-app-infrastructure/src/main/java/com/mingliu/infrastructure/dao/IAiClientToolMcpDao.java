package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientToolMcp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端工具MCP数据访问层
 */
@Mapper
public interface IAiClientToolMcpDao extends BaseMapper<AiClientToolMcp> {



    /**
     * 根据MCP ID查询工具MCP配置
     * @param mcpId MCP ID
     * @return 工具MCP配置
     */
    @Select("SELECT * FROM ai_client_tool_mcp WHERE mcp_id = #{mcpId} AND status = 1")
    AiClientToolMcp queryByMcpId(String mcpId);

}
package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端配置数据访问层
 */
@Mapper
public interface IAiClientConfigDao extends BaseMapper<AiClientConfig> {


    /**
     * 根据源类型和源ID查询配置
     * @param sourceType 源类型
     * @param sourceId 源ID
     * @return 配置列表
     */
    @Select("SELECT * FROM ai_client_config WHERE source_type = #{sourceType} AND source_id = #{sourceId} AND status = 1")
    List<AiClientConfig> queryBySourceTypeAndId(String sourceType, String sourceId);

}
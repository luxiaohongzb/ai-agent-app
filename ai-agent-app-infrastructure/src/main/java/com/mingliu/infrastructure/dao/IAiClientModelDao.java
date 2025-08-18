package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端模型数据访问层
 */
@Mapper
public interface IAiClientModelDao extends BaseMapper<AiClientModel> {


    /**
     * 根据模型ID查询模型配置
     * @param modelId 模型ID
     * @return 模型配置
     */
    @Select("SELECT * FROM ai_client_model WHERE model_id = #{modelId} AND status = 1")
    AiClientModel queryByModelId(String modelId);

}
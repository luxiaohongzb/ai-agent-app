package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端API数据访问层
 */
@Mapper
public interface IAiClientApiDao extends BaseMapper<AiClientApi> {



    /**
     * 根据API ID查询API配置
     * @param apiId API ID
     * @return API配置
     */
    @Select("SELECT * FROM ai_client_api WHERE api_id = #{apiId} AND status = 1")
    AiClientApi queryByApiId(String apiId);

}
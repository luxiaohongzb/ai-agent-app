package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端数据访问层
 */
@Mapper
public interface IAiClientDao extends BaseMapper<AiClient> {

    /**
     * 根据客户端ID查询客户端信息
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    @Select("SELECT * FROM ai_client WHERE client_id = #{clientId} AND status = 1")
    AiClient queryByClientId(String clientId);

    /**
     * 查询所有有效的客户端
     * @return 客户端列表
     */
    @Select("SELECT * FROM ai_client WHERE status = 1")
    List<AiClient> queryAllActive();

}
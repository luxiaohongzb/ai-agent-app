package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientAdvisor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI客户端顾问数据访问层
 */
@Mapper
public interface IAiClientAdvisorDao extends BaseMapper<AiClientAdvisor> {


    /**
     * 根据顾问ID查询顾问配置
     * @param advisorId 顾问ID
     * @return 顾问配置
     */
    @Select("SELECT * FROM ai_client_advisor WHERE advisor_id = #{advisorId} AND status = 1")
    AiClientAdvisor queryByAdvisorId(String advisorId);

}
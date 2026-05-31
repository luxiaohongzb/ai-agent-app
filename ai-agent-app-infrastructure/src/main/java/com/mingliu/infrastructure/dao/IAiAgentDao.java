package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * AI代理数据访问层
 */
@Mapper
public interface IAiAgentDao extends BaseMapper<AiAgent> {


}
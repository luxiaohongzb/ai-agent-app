package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiClientRagOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI客户端RAG订单数据访问层
 */
@Mapper
public interface IAiClientRagOrderDao extends BaseMapper<AiClientRagOrder> {



}
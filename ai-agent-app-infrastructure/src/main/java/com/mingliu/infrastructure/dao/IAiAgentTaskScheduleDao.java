package com.mingliu.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingliu.infrastructure.dao.po.AiAgentTaskSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI代理任务调度数据访问层
 */
@Mapper
public interface IAiAgentTaskScheduleDao extends BaseMapper<AiAgentTaskSchedule> {

    /**
     * 根据条件查询任务调度列表
     * @param aiAgentTaskSchedule 查询条件
     * @return 任务调度列表
     */
    List<AiAgentTaskSchedule> queryTaskScheduleList(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 根据ID查询任务调度
     * @param id 任务调度ID
     * @return 任务调度
     */
    AiAgentTaskSchedule queryTaskScheduleById(Long id);
}
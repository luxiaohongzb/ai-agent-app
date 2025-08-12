package com.mingliu.domain.agent.service.armory.business.data;

import com.mingliu.domain.agent.model.entity.ArmoryCommandEntity;
import com.mingliu.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;

import java.util.List;


public interface ILoadDataStrategy {

    void loadData(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext);

}

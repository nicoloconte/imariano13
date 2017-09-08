package com.wunderground.pws.service;

import java.util.Map;

import com.wunderground.pws.model.ConditionTrend;
import com.wunderground.pws.model.entities.CurrentObservation;

public interface ConditionsService {

	public CurrentObservation getActualCondition();

	public void saveCondition(CurrentObservation currentObservation);

	public Map<String, ConditionTrend> getTrend();

}

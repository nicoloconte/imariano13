package com.wunderground.pws.service;

import com.wunderground.pws.model.entities.CurrentObservation;

public interface ConditionsService {

	public CurrentObservation getActualCondition();

}

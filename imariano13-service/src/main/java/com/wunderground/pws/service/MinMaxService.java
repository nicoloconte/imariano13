package com.wunderground.pws.service;

import java.util.List;
import java.util.Map;

import com.wunderground.pws.model.entities.ObservationMinMax;

public interface MinMaxService {

	public Map<String, List<ObservationMinMax>> getMinMax();

}

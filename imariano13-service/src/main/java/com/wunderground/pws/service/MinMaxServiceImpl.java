package com.wunderground.pws.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wunderground.pws.model.entities.ObservationMinMax;
import com.wunderground.pws.persistence.repositories.MinMaxRepository;

@Service
public class MinMaxServiceImpl implements MinMaxService {

	private static final String YEARMOUNTH = "yearmounth";

	private static final String YEAR = "year";

	private static final String MOUNTH = "mounth";

	private static final String ALLTIME = "alltime";

	private static final Logger log = LoggerFactory.getLogger(MinMaxServiceImpl.class);

	@Autowired
	private MinMaxRepository minMaxRepository;

	@Override
	public Map<String, List<ObservationMinMax>> getMinMax() {
		log.info("starting getting MIN MAX");
		Iterable<ObservationMinMax> minMaxIterator = minMaxRepository.findAll();
		return buildMinMaxEstremes(minMaxIterator);
	}

	private Map<String, List<ObservationMinMax>> buildMinMaxEstremes(Iterable<ObservationMinMax> minMaxIterator) {
		Map<String, List<ObservationMinMax>> minMaxMap = new HashMap<>();
		initializeMap(minMaxMap);
		for (ObservationMinMax observationMinMax : minMaxIterator) {
			log.debug("elaborating {} ", observationMinMax);
			String id = observationMinMax.getId();
			switch (id.length()) {
			case 3:
				minMaxMap.get(ALLTIME).add(observationMinMax);
				break;
			case 4:
				minMaxMap.get(MOUNTH).add(observationMinMax);
				break;
			case 7:
				minMaxMap.get(YEAR).add(observationMinMax);
				break;
			case 8:
				minMaxMap.get(YEARMOUNTH).add(observationMinMax);
				break;
			default:
				break;
			}
		}
		return minMaxMap;
	}

	private void initializeMap(Map<String, List<ObservationMinMax>> minMaxMap) {
		minMaxMap.put(ALLTIME, new ArrayList<>());
		minMaxMap.put(YEAR, new ArrayList<>());
		minMaxMap.put(MOUNTH, new ArrayList<>());
		minMaxMap.put(YEARMOUNTH, new ArrayList<>());
	}
}

package com.wunderground.pws.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.model.entities.ObservationMinMax;
import com.wunderground.pws.persistence.repositories.MinMaxRepository;

@Service
public class MinMaxServiceImpl implements MinMaxService {

	private static final Logger log = LoggerFactory.getLogger(MinMaxServiceImpl.class);

	private static final String YEARMOUNTH = "yearmounth";
	private static final String YEAR = "year";
	private static final String MOUNTH = "mounth";
	private static final String ALLTIME = "alltime";
	private static final String MAX = "MAX";
	private static final String MIN = "MIN";

	@Autowired
	private MinMaxRepository minMaxRepository;

	@Override
	public Map<String, List<ObservationMinMax>> getMinMax() {
		log.info("starting getting MIN MAX");
		Iterable<ObservationMinMax> minMaxIterator = minMaxRepository.findAll();
		return buildMinMaxEstremes(minMaxIterator);
	}

	@Override
	public void updateMinMax(CurrentObservation currentObservation) {
		String month = getMonth(currentObservation.getObservationTime());
		String year = getYear(currentObservation.getObservationTime());

		saveMinMax(currentObservation, MAX);
		saveMinMax(currentObservation, MIN);

		saveMinMax(currentObservation, year.concat(MIN));
		saveMinMax(currentObservation, year.concat(MAX));

		saveMinMax(currentObservation, month.concat(MIN));
		saveMinMax(currentObservation, month.concat(MAX));

		saveMinMax(currentObservation, year.concat(month.concat(MIN)));
		saveMinMax(currentObservation, year.concat(month.concat(MAX)));
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

	private void saveMinMax(CurrentObservation currentObservation, String id) {
		log.info("getting {} ", id);
		ObservationMinMax minMax = minMaxRepository.findOne(id);
		log.debug("{} {}", id, minMax);
		boolean newMinMax = false;
		if (minMax == null) {
			minMax = new ObservationMinMax();
			minMax.setId(id);
			mapNewMinMax(minMax, currentObservation);
			newMinMax = true;
		} else {
			if (id.contains(MAX)) {
				newMinMax = mapMax(minMax, currentObservation);
			} else {
				newMinMax = mapMin(minMax, currentObservation);
			}
		}
		minMax.setTimestampUpdate(ZonedDateTime.now());
		if (newMinMax) {
			log.debug("NEW MIN / MAX founded!");
			log.debug("saving {} {}", id, minMax);
			minMaxRepository.save(minMax);
		} else {
			log.debug("NO new MIN / MAX founded!");
		}
	}

	private boolean mapMax(ObservationMinMax minMax, CurrentObservation currentObservation) {
		boolean newMinMax = false;
		log.debug("checking dewpointC: current[{}]  max[{}]", currentObservation.getDewpointC(), minMax.getDewpointC());
		if (currentObservation.getDewpointC() > minMax.getDewpointC()) {
			newMinMax = true;
			minMax.setDewpointC(currentObservation.getDewpointC());
			minMax.setDewpointCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking feelslikeC: current[{}]  max[{}]", currentObservation.getFeelslikeC(),
				minMax.getFeelslikeC());
		if (currentObservation.getFeelslikeC() > minMax.getFeelslikeC()) {
			newMinMax = true;
			minMax.setFeelslikeC(currentObservation.getFeelslikeC());
			minMax.setFeelslikeCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precip1hrMetric: current[{}]  max[{}]", currentObservation.getPrecip1hrMetric(),
				minMax.getPrecip1hrMetric());
		if (currentObservation.getPrecip1hrMetric() > minMax.getPrecip1hrMetric()) {
			newMinMax = true;
			minMax.setPrecip1hrMetric(currentObservation.getPrecip1hrMetric());
			minMax.setPrecip1hrMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precipTodayMetric: current[{}]  max[{}]", currentObservation.getPrecipTodayMetric(),
				minMax.getPrecipTodayMetric());
		if (currentObservation.getPrecipTodayMetric() > minMax.getPrecipTodayMetric()) {
			newMinMax = true;
			minMax.setPrecipTodayMetric(currentObservation.getPrecipTodayMetric());
			minMax.setPrecipTodayMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking pressureMb: current[{}]  max[{}]", currentObservation.getPressureMb(),
				minMax.getPressureMb());
		if (currentObservation.getPressureMb() > minMax.getPressureMb()) {
			newMinMax = true;
			minMax.setPressureMb(currentObservation.getPressureMb());
			minMax.setPressureMbObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking solarradiation: current[{}]  max[{}]", currentObservation.getSolarradiation(),
				minMax.getSolarradiation());
		if (currentObservation.getSolarradiation() > minMax.getSolarradiation()) {
			newMinMax = true;
			minMax.setSolarradiation(currentObservation.getSolarradiation());
			minMax.setSolarradiationObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking tempC: current[{}]  max[{}]", currentObservation.getTempC(), minMax.getTempC());
		if (currentObservation.getTempC() > minMax.getTempC()) {
			newMinMax = true;
			minMax.setTempC(currentObservation.getTempC());
			minMax.setTempCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking UV: current[{}]  max[{}]", currentObservation.getuV(), minMax.getuV());
		if (currentObservation.getuV() > minMax.getuV()) {
			newMinMax = true;
			minMax.setuV(currentObservation.getuV());
			minMax.setuVObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windGustKph: current[{}]  max[{}]", currentObservation.getWindGustKph(),
				minMax.getWindGustKph());
		if (currentObservation.getWindGustKph() > minMax.getWindGustKph()) {
			newMinMax = true;
			minMax.setWindGustKph(currentObservation.getWindGustKph());
			minMax.setWindGustKphObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windKph: current[{}]  max[{}]", currentObservation.getWindKph(), minMax.getWindKph());
		if (currentObservation.getWindKph() > minMax.getWindKph()) {
			newMinMax = true;
			minMax.setWindKph(currentObservation.getWindKph());
			minMax.setWindKphObservationTime(currentObservation.getObservationTime());
		}
		return newMinMax;
	}

	private boolean mapMin(ObservationMinMax minMax, CurrentObservation currentObservation) {
		boolean newMinMax = false;
		log.debug("checking dewpointC: current[{}]  min[{}]", currentObservation.getDewpointC(), minMax.getDewpointC());
		if (currentObservation.getDewpointC() < minMax.getDewpointC()) {
			newMinMax = true;
			minMax.setDewpointC(currentObservation.getDewpointC());
			minMax.setDewpointCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking feelslikeC: current[{}]  min[{}]", currentObservation.getFeelslikeC(),
				minMax.getFeelslikeC());
		if (currentObservation.getFeelslikeC() < minMax.getFeelslikeC()) {
			newMinMax = true;
			minMax.setFeelslikeC(currentObservation.getFeelslikeC());
			minMax.setFeelslikeCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precip1hrMetric: current[{}]  min[{}]", currentObservation.getPrecip1hrMetric(),
				minMax.getPrecip1hrMetric());
		if (currentObservation.getPrecip1hrMetric() < minMax.getPrecip1hrMetric()) {
			newMinMax = true;
			minMax.setPrecip1hrMetric(currentObservation.getPrecip1hrMetric());
			minMax.setPrecip1hrMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precipTodayMetric: current[{}]  min[{}]", currentObservation.getPrecipTodayMetric(),
				minMax.getPrecipTodayMetric());
		if (currentObservation.getPrecipTodayMetric() < minMax.getPrecipTodayMetric()) {
			newMinMax = true;
			minMax.setPrecipTodayMetric(currentObservation.getPrecipTodayMetric());
			minMax.setPrecipTodayMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking pressureMb: current[{}]  min[{}]", currentObservation.getPressureMb(),
				minMax.getPressureMb());
		if (currentObservation.getPressureMb() < minMax.getPressureMb()) {
			newMinMax = true;
			minMax.setPressureMb(currentObservation.getPressureMb());
			minMax.setPressureMbObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking solarradiation: current[{}]  min[{}]", currentObservation.getSolarradiation(),
				minMax.getSolarradiation());
		if (currentObservation.getSolarradiation() < minMax.getSolarradiation()) {
			newMinMax = true;
			minMax.setSolarradiation(currentObservation.getSolarradiation());
			minMax.setSolarradiationObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking tempC: current[{}]  min[{}]", currentObservation.getTempC(), minMax.getTempC());
		if (currentObservation.getTempC() < minMax.getTempC()) {
			newMinMax = true;
			minMax.setTempC(currentObservation.getTempC());
			minMax.setTempCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking UV: current[{}]  min[{}]", currentObservation.getuV(), minMax.getuV());
		if (currentObservation.getuV() < minMax.getuV()) {
			newMinMax = true;
			minMax.setuV(currentObservation.getuV());
			minMax.setuVObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windGustKph: current[{}]  min[{}]", currentObservation.getWindGustKph(),
				minMax.getWindGustKph());
		if (currentObservation.getWindGustKph() < minMax.getWindGustKph()) {
			newMinMax = true;
			minMax.setWindGustKph(currentObservation.getWindGustKph());
			minMax.setWindGustKphObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windKph: current[{}]  min[{}]", currentObservation.getWindKph(), minMax.getWindKph());
		if (currentObservation.getWindKph() < minMax.getWindKph()) {
			newMinMax = true;
			minMax.setWindKph(currentObservation.getWindKph());
			minMax.setWindKphObservationTime(currentObservation.getObservationTime());
		}
		return newMinMax;
	}

	private void mapNewMinMax(ObservationMinMax minMax, CurrentObservation currentObservation) {
		minMax.setDewpointC(currentObservation.getDewpointC());
		minMax.setDewpointCObservationTime(currentObservation.getObservationTime());
		minMax.setFeelslikeC(currentObservation.getFeelslikeC());
		minMax.setFeelslikeCObservationTime(currentObservation.getObservationTime());
		minMax.setPrecip1hrMetric(currentObservation.getPrecip1hrMetric());
		minMax.setPrecip1hrMetricObservationTime(currentObservation.getObservationTime());
		minMax.setPrecipTodayMetric(currentObservation.getPrecipTodayMetric());
		minMax.setPrecipTodayMetricObservationTime(currentObservation.getObservationTime());
		minMax.setPressureMb(currentObservation.getPressureMb());
		minMax.setPressureMbObservationTime(currentObservation.getObservationTime());
		minMax.setSolarradiation(currentObservation.getSolarradiation());
		minMax.setSolarradiationObservationTime(currentObservation.getObservationTime());
		minMax.setTempC(currentObservation.getTempC());
		minMax.setTempCObservationTime(currentObservation.getObservationTime());
		minMax.setuV(currentObservation.getuV());
		minMax.setuVObservationTime(currentObservation.getObservationTime());
		minMax.setWindGustKph(currentObservation.getWindGustKph());
		minMax.setWindGustKphObservationTime(currentObservation.getObservationTime());
		minMax.setWindKph(currentObservation.getWindKph());
		minMax.setWindKphObservationTime(currentObservation.getObservationTime());
	}

	private String getYear(ZonedDateTime observationTime) {
		String year = String.valueOf(observationTime.getYear());
		log.debug("getYear {}", year);
		return year;
	}

	private String getMonth(ZonedDateTime observationTime) {
		String month = String.valueOf(observationTime.getMonthValue());
		log.debug("getMonth {}", month);
		return month;
	}
}

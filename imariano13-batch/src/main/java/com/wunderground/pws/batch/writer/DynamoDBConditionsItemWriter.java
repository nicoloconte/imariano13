package com.wunderground.pws.batch.writer;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.model.entities.LastObservation;
import com.wunderground.pws.model.entities.ObservationMinMax;
import com.wunderground.pws.persistence.repositories.ConditionRepository;
import com.wunderground.pws.persistence.repositories.LastObservationRepository;
import com.wunderground.pws.persistence.repositories.MinMaxRepository;

public class DynamoDBConditionsItemWriter implements ItemWriter<CurrentObservation> {

	private static final Logger log = LoggerFactory.getLogger(DynamoDBConditionsItemWriter.class);

	private static final String MAX = "MAX";
	private static final String MIN = "MIN";

	private ConditionRepository conditionRepository;
	private MinMaxRepository minMaxRepository;
	private LastObservationRepository lastObservationRepository;

	public DynamoDBConditionsItemWriter(ConditionRepository conditionRepository, MinMaxRepository minMaxRepository, LastObservationRepository lastObservationRepository) {
		super();
		this.conditionRepository = conditionRepository;
		this.minMaxRepository = minMaxRepository;
		this.lastObservationRepository = lastObservationRepository;
	}

	@Override
	public void write(List<? extends CurrentObservation> items) throws Exception {
		log.debug("saving {} items", items.size());
		for (CurrentObservation currentObservation : items) {
			currentObservation = conditionRepository.save(currentObservation);
			saveLastObservation(currentObservation);
			saveMinMax(currentObservation);
		}
	}

	private void saveLastObservation(CurrentObservation currentObservation) {
		log.info("saving last observation");
		LastObservation lastObservation = new LastObservation();
		long count = lastObservationRepository.count();
		log.debug("last observation count {}", count);
		switch ((int) count) {
		case 0:
			break;
		case 1:
			lastObservation = lastObservationRepository.findAll().iterator().next();
		default:
			lastObservationRepository.deleteAll();
			break;
		}
		log.debug("saving NEW last observation id {}", currentObservation.getId());
		lastObservation.setId(currentObservation.getId());
		lastObservationRepository.save(lastObservation);
	}

	private void saveMinMax(CurrentObservation currentObservation) {
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
		if(currentObservation.getDewpointC() > minMax.getDewpointC()){
			newMinMax = true;
			minMax.setDewpointC(currentObservation.getDewpointC());
			minMax.setDewpointCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking feelslikeC: current[{}]  max[{}]", currentObservation.getFeelslikeC(), minMax.getFeelslikeC());
		if(currentObservation.getFeelslikeC() > minMax.getFeelslikeC()){
			newMinMax = true;
			minMax.setFeelslikeC(currentObservation.getFeelslikeC());
			minMax.setFeelslikeCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precip1hrMetric: current[{}]  max[{}]", currentObservation.getPrecip1hrMetric(), minMax.getPrecip1hrMetric());
		if(currentObservation.getPrecip1hrMetric() > minMax.getPrecip1hrMetric()){
			newMinMax = true;
			minMax.setPrecip1hrMetric(currentObservation.getPrecip1hrMetric());
			minMax.setPrecip1hrMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precipTodayMetric: current[{}]  max[{}]", currentObservation.getPrecipTodayMetric(), minMax.getPrecipTodayMetric());
		if(currentObservation.getPrecipTodayMetric() > minMax.getPrecipTodayMetric()){
			newMinMax = true;
			minMax.setPrecipTodayMetric(currentObservation.getPrecipTodayMetric());
			minMax.setPrecipTodayMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking pressureMb: current[{}]  max[{}]", currentObservation.getPressureMb(), minMax.getPressureMb());
		if(currentObservation.getPressureMb() > minMax.getPressureMb()){
			newMinMax = true;
			minMax.setPressureMb(currentObservation.getPressureMb());
			minMax.setPressureMbObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking solarradiation: current[{}]  max[{}]", currentObservation.getSolarradiation(), minMax.getSolarradiation());
		if(currentObservation.getSolarradiation() > minMax.getSolarradiation()){
			newMinMax = true;
			minMax.setSolarradiation(currentObservation.getSolarradiation());
			minMax.setSolarradiationObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking tempC: current[{}]  max[{}]", currentObservation.getTempC(), minMax.getTempC());
		if(currentObservation.getTempC() > minMax.getTempC()){
			newMinMax = true;
			minMax.setTempC(currentObservation.getTempC());
			minMax.setTempCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking UV: current[{}]  max[{}]", currentObservation.getuV(), minMax.getuV());
		if(currentObservation.getuV() > minMax.getuV()){
			newMinMax = true;
			minMax.setuV(currentObservation.getuV());
			minMax.setuVObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windGustKph: current[{}]  max[{}]", currentObservation.getWindGustKph(), minMax.getWindGustKph());
		if(currentObservation.getWindGustKph() > minMax.getWindGustKph()){
			newMinMax = true;
			minMax.setWindGustKph(currentObservation.getWindGustKph());
			minMax.setWindGustKphObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windKph: current[{}]  max[{}]", currentObservation.getWindKph(), minMax.getWindKph());
		if(currentObservation.getWindKph() > minMax.getWindKph()){
			newMinMax = true;
			minMax.setWindKph(currentObservation.getWindKph());
			minMax.setWindKphObservationTime(currentObservation.getObservationTime());
		}
		return newMinMax;
	}

	private boolean mapMin(ObservationMinMax minMax, CurrentObservation currentObservation) {
		boolean newMinMax = false;
		log.debug("checking dewpointC: current[{}]  min[{}]", currentObservation.getDewpointC(), minMax.getDewpointC());
		if(currentObservation.getDewpointC() < minMax.getDewpointC()){
			newMinMax = true;
			minMax.setDewpointC(currentObservation.getDewpointC());
			minMax.setDewpointCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking feelslikeC: current[{}]  min[{}]", currentObservation.getFeelslikeC(), minMax.getFeelslikeC());
		if(currentObservation.getFeelslikeC() < minMax.getFeelslikeC()){
			newMinMax = true;
			minMax.setFeelslikeC(currentObservation.getFeelslikeC());
			minMax.setFeelslikeCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precip1hrMetric: current[{}]  min[{}]", currentObservation.getPrecip1hrMetric(), minMax.getPrecip1hrMetric());
		if(currentObservation.getPrecip1hrMetric() < minMax.getPrecip1hrMetric()){
			newMinMax = true;
			minMax.setPrecip1hrMetric(currentObservation.getPrecip1hrMetric());
			minMax.setPrecip1hrMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking precipTodayMetric: current[{}]  min[{}]", currentObservation.getPrecipTodayMetric(), minMax.getPrecipTodayMetric());
		if(currentObservation.getPrecipTodayMetric() < minMax.getPrecipTodayMetric()){
			newMinMax = true;
			minMax.setPrecipTodayMetric(currentObservation.getPrecipTodayMetric());
			minMax.setPrecipTodayMetricObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking pressureMb: current[{}]  min[{}]", currentObservation.getPressureMb(), minMax.getPressureMb());
		if(currentObservation.getPressureMb() < minMax.getPressureMb()){
			newMinMax = true;
			minMax.setPressureMb(currentObservation.getPressureMb());
			minMax.setPressureMbObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking solarradiation: current[{}]  min[{}]", currentObservation.getSolarradiation(), minMax.getSolarradiation());
		if(currentObservation.getSolarradiation() < minMax.getSolarradiation()){
			newMinMax = true;
			minMax.setSolarradiation(currentObservation.getSolarradiation());
			minMax.setSolarradiationObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking tempC: current[{}]  min[{}]", currentObservation.getTempC(), minMax.getTempC());
		if(currentObservation.getTempC() < minMax.getTempC()){
			newMinMax = true;
			minMax.setTempC(currentObservation.getTempC());
			minMax.setTempCObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking UV: current[{}]  min[{}]", currentObservation.getuV(), minMax.getuV());
		if(currentObservation.getuV() < minMax.getuV()){
			newMinMax = true;
			minMax.setuV(currentObservation.getuV());
			minMax.setuVObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windGustKph: current[{}]  min[{}]", currentObservation.getWindGustKph(), minMax.getWindGustKph());
		if(currentObservation.getWindGustKph() < minMax.getWindGustKph()){
			newMinMax = true;
			minMax.setWindGustKph(currentObservation.getWindGustKph());
			minMax.setWindGustKphObservationTime(currentObservation.getObservationTime());
		}
		log.debug("checking windKph: current[{}]  min[{}]", currentObservation.getWindKph(), minMax.getWindKph());
		if(currentObservation.getWindKph() < minMax.getWindKph()){
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

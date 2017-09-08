package com.wunderground.pws.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wunderground.pws.model.ConditionTrend;
import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.model.entities.LastObservation;
import com.wunderground.pws.persistence.repositories.ConditionRepository;
import com.wunderground.pws.persistence.repositories.LastObservationRepository;

@Service
public class ConditionsServiceImpl implements ConditionsService, InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(ConditionsServiceImpl.class);

	private static final Integer OBSERVATION_PER_HOUR = 12;

	@Autowired
	private LastObservationRepository lastObservationRepository;
	@Autowired
	private ConditionRepository conditionRepository;

	@Autowired
	private MinMaxService minMaxService;

	private CurrentObservation lastObservation;
	private List<CurrentObservation> oneDayObservations;

	@Override
	public CurrentObservation getActualCondition() {
		log.info("getting actual condition");
		log.debug("current observation time is {}", this.lastObservation.getObservationTime());
		return this.lastObservation;
	}

	@Override
	public void saveCondition(CurrentObservation currentObservation) {
		log.info("saving observation {}", currentObservation);
		conditionRepository.save(currentObservation);
		addElementToCache(currentObservation);
		saveLastObservation(currentObservation);
		minMaxService.updateMinMax(currentObservation);
		log.info("saving observation end");
	}

	@Override
	public Map<String, ConditionTrend> getTrend() {
		log.info("getting trend");
		Map<String, ConditionTrend> trendMap = new HashMap<>();
		log.debug("this.oneDayObservations.size() {}", this.oneDayObservations.size());
		CurrentObservation to = this.oneDayObservations.get(this.oneDayObservations.size() - 1);
		CurrentObservation from30 = this.oneDayObservations
				.get(this.oneDayObservations.size() - (OBSERVATION_PER_HOUR / 2) - 1);
		log.debug("from30 {}", from30);
		CurrentObservation from60 = this.oneDayObservations
				.get(this.oneDayObservations.size() - (OBSERVATION_PER_HOUR) - 1);
		log.debug("from60 {}", from60);
		CurrentObservation from180 = this.oneDayObservations
				.get(this.oneDayObservations.size() - (OBSERVATION_PER_HOUR * 2) - 1);
		log.debug("from180 {}", from180);
		trendMap.put("30", buildTrend(from30, to));
		trendMap.put("60", buildTrend(from60, to));
		trendMap.put("180", buildTrend(from180, to));
		return trendMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("initializing cache elements");
		this.lastObservation = getLastObservation();
		List<CurrentObservation> conditions = conditionRepository
				.findByObservationTimeBetween(ZonedDateTime.now().minusDays(1), ZonedDateTime.now());
		log.debug("conditions size is {}", conditions.size());
		this.oneDayObservations = new ArrayList<>();
		this.oneDayObservations.addAll(conditions);
		sortOneDayObservations();
		log.debug("oneDayObservations size is {}", oneDayObservations.size());
	}

	private ConditionTrend buildTrend(CurrentObservation from, CurrentObservation to) {
		log.debug("building trend");
		ConditionTrend trend = new ConditionTrend();
		double deltaPressureMb = to.getPressureMb() - from.getPressureMb();
		deltaPressureMb = BigDecimal.valueOf(deltaPressureMb).setScale(1, RoundingMode.HALF_UP).doubleValue();
		trend.setDeltaPressureMb(deltaPressureMb);

		trend.setDeltaRelativeHumidity(new Double(to.getRelativeHumidity().replaceAll("%", ""))
				- new Double(from.getRelativeHumidity().replaceAll("%", "")));

		double deltaSolarradiation = to.getSolarradiation() - from.getSolarradiation();
		deltaSolarradiation = BigDecimal.valueOf(deltaSolarradiation).setScale(1, RoundingMode.HALF_UP).doubleValue();
		trend.setDeltaSolarradiation(deltaSolarradiation);

		double deltaTempC = to.getTempC() - from.getTempC();
		deltaTempC = BigDecimal.valueOf(deltaTempC).setScale(1, RoundingMode.HALF_UP).doubleValue();
		trend.setDeltaTempC(deltaTempC);

		trend.setDeltaUv(to.getuV() - from.getuV());
		log.debug("trend is {}", trend);
		return trend;
	}

	private void addElementToCache(CurrentObservation currentObservation) {
		this.oneDayObservations.remove(0);
		this.oneDayObservations.add(currentObservation);
		sortOneDayObservations();
		log.debug("oneDayObservations size is {}", oneDayObservations.size());
	}

	private void sortOneDayObservations() {
		this.oneDayObservations.sort(new Comparator<CurrentObservation>() {
			@Override
			public int compare(CurrentObservation o1, CurrentObservation o2) {
				if (o1.getObservationTime().isAfter(o2.getObservationTime())) {
					return 1;
				}
				return -1;
			}
		});
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
		this.lastObservation = currentObservation;
	}

	private CurrentObservation getLastObservation() {
		Iterator<LastObservation> lastObservationIterator = lastObservationRepository.findAll().iterator();
		if (!lastObservationIterator.hasNext()) {
			log.error("NO last observation FOUNDED!");
			return null;
		}
		LastObservation lastObservation = lastObservationIterator.next();
		log.debug("last observation is {}", lastObservation.getId());
		CurrentObservation currentObservation = conditionRepository.findOne(lastObservation.getId());
		if (currentObservation == null) {
			log.error("NO observation founded with id " + lastObservation.getId());
			return null;
		}
		return currentObservation;
	}

}

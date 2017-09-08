package com.wunderground.pws.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wunderground.pws.model.entities.MoonPhase;
import com.wunderground.pws.persistence.repositories.AstronomyRepository;

@Service
public class AstronomyServiceImpl implements AstronomyService {

	private static final Logger log = LoggerFactory.getLogger(AstronomyServiceImpl.class);

	@Autowired
	private AstronomyRepository astronomyRepository;

	@Override
	public MoonPhase getAstronomy() {
		log.info("start getting astronomy");
		MoonPhase moonPhase = null;
		List<MoonPhase> sortedMoonPhases = getSortedMoonPhases();
		if (sortedMoonPhases == null) {
			log.error("no Astronomy founded!");
			throw new RuntimeException("no Astronomy founded!");
		} else if (sortedMoonPhases.size() == 1) {
			moonPhase = sortedMoonPhases.get(0);
		} else {
			moonPhase = sortedMoonPhases.get(1);
		}
		log.debug("astronomy is {}", moonPhase);
		return moonPhase;
	}

	@Override
	public Integer getDayLengthTrend() {
		log.info("getting solar trend");
		List<MoonPhase> sortedMoonPhases = getSortedMoonPhases();
		if (sortedMoonPhases == null || sortedMoonPhases.size() == 1) {
			log.error("unable to calculate trend");
			throw new RuntimeException("unable to calculate trend");
		}
		MoonPhase yesterday = sortedMoonPhases.get(0);
		MoonPhase today = sortedMoonPhases.get(1);
		int todaySolarDuration = today.getDayLength();
		log.debug("todaySolarDuration duration is {}", todaySolarDuration);
		int yesterdaySolarDuration = yesterday.getDayLength();
		log.debug("yesterdaySolarDuration duration is {}", yesterdaySolarDuration);
		int delta = todaySolarDuration - yesterdaySolarDuration;
		log.debug("delta duration is {}", delta);
		return delta;
	}

	@Override
	public void saveAstronomy(MoonPhase moonPhase) {
		log.info("saving last astronomy");
		long count = astronomyRepository.count();
		log.debug("last astronomy count {}", count);
		switch ((int) count) {
		case 0:
			break;
		case 1:
			break;
		default:
			List<MoonPhase> astronomyList = getSortedMoonPhases();
			log.debug("deleting astronomy {}", astronomyList.get(0));
			astronomyRepository.delete(astronomyList.get(0));
			break;
		}
		log.debug("saving NEW last astronomy {}", moonPhase);
		astronomyRepository.save(moonPhase);
	}

	private List<MoonPhase> getSortedMoonPhases() {
		Iterable<MoonPhase> allAstronomy = astronomyRepository.findAll();
		List<MoonPhase> astronomyList = new ArrayList<>();
		allAstronomy.iterator().forEachRemaining(astronomyList::add);
		timestampSort(astronomyList);
		return astronomyList;
	}

	private void timestampSort(List<MoonPhase> astronomyList) {
		Collections.sort(astronomyList, new Comparator<MoonPhase>() {
			@Override
			public int compare(MoonPhase o1, MoonPhase o2) {
				if (o1.getTimestamp().isAfter(o2.getTimestamp())) {
					return 1;
				}
				return -1;
			}
		});
	}
}

package com.wunderground.pws.service;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.model.entities.LastObservation;
import com.wunderground.pws.persistence.repositories.ConditionRepository;
import com.wunderground.pws.persistence.repositories.LastObservationRepository;

@Service
public class ConditionsServiceImpl implements ConditionsService {

	private static final Logger log = LoggerFactory.getLogger(ConditionsServiceImpl.class);
	
	@Autowired
	private LastObservationRepository lastObservationRepository;
	@Autowired
	private ConditionRepository conditionRepository;
	
	@Override
	public CurrentObservation getActualCondition(){
		log.info("getting actual condition");
		Iterator<LastObservation> lastObservationIterator = lastObservationRepository.findAll().iterator();
		if(!lastObservationIterator.hasNext()){
			throw new RuntimeException("NO last observation FOUNDED!");
		}
		LastObservation lastObservation = lastObservationIterator.next();
		log.debug("last observation is {}", lastObservation.getId());
		CurrentObservation currentObservation = conditionRepository.findOne(lastObservation.getId());
		if(currentObservation == null){
			throw new RuntimeException("NO observation founded with id " + lastObservation.getId());
		}
		log.debug("current observation time is {}", currentObservation.getObservationTime());
		return currentObservation;
	}
}

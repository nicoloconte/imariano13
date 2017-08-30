package com.wunderground.pws.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.wunderground.pws.model.Condition;
import com.wunderground.pws.model.CurrentObservation;

public class ConditionItemProcessor implements ItemProcessor<Condition, CurrentObservation>{

	private static final Logger log = LoggerFactory.getLogger(ConditionItemProcessor.class);
	
	@Override
	public CurrentObservation process(final Condition condition) throws Exception {
		log.info("start processing");
		log.debug(condition.toString());
		log.info("end processing");
		return condition.getCurrentObservation();
	}

}

package com.wunderground.pws.batch.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.service.ConditionsService;

public class DynamoDBConditionsItemWriter implements ItemWriter<CurrentObservation> {

	private static final Logger log = LoggerFactory.getLogger(DynamoDBConditionsItemWriter.class);

	private ConditionsService conditionsService;

	public DynamoDBConditionsItemWriter(ConditionsService conditionsService) {
		super();
		this.conditionsService = conditionsService;
	}

	@Override
	public void write(List<? extends CurrentObservation> items) throws Exception {
		log.debug("saving {} items", items.size());
		for (CurrentObservation currentObservation : items) {
			conditionsService.saveCondition(currentObservation);
		}
	}

}
